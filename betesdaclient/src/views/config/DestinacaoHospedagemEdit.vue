<template>
  <div>
    <v-dialog v-model="dialogVisible" width="500">
        <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          {{titulo}}
        </v-card-title>
        <v-card-text>
          <v-container grid-list-md>
            <v-layout wrap>
              <v-flex xs12 sm12 md12>
                <v-text-field v-model="form.descricao" :error-messages="getErrors('descricao')" label="Descrição" required></v-text-field>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions class="grey lighten-4"> 
          <v-spacer></v-spacer>
          <v-btn small dark color="cyan darken-4" @click.native="save">
            Ok
          </v-btn>
          <v-btn small color="secondary" @click.native="close(false)">
            Cancelar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>

export default {
  name: 'DestinacaoHospedagemEdit',
  
  props: {
    
  },

  data: () =>({
    titulo : "Incluir/Alterar Destinação de Hospedagem",
    form:{},
    dados: [],
    errors:[],
    dialogVisible : false
  }),

  created(){
    this.reset()
  },

  methods: {
    openDialog(form){
      this.reset()
      if (!form){
        this.titulo = "Incluir Destinação de Hospedagem"
        this.reset()
      } else {
        this.titulo = "Alterar Destinação de Hospedagem"
        this.form = form
      }
      this.dialogVisible = true
    },

    close(value){
      this.dialogVisible = false
      this.$emit('close',false)
    },

    save(evt) {
      this.errors.descricao = [];

      let uri = petra.base_uri+"/app/destinacao_hospedagem";
      axios.post(uri, this.form)
          .then(response => { 
            this.dialogVisible = false
            this.$emit('close',true)
            this.$emit('save',response.data)
          }).catch(error => {
            this.errors = petra.tratarErros(error);
          });
    },

    reset(evt){
      this.form = {
        id : null,
        descricao : null
      },
      this.errors = []
    },

    getErrors(fieldName){
      return petra.getErrors(fieldName, this.errors)
    }

  }
}
</script>