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
              <v-flex xs12 sm12 md12>
                  <v-select v-model="form.disponivel" :items="itensSimNao" label="Disponível?" required></v-select>
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
  name: 'SituacaoLeitoEdit',
  
  props: {
    
  },

  data: () =>({
    titulo : "Incluir/Alterar Situação de Leito",
    form:{},
    dados: [],
    errors:[],
    dialogVisible : false,

    itensSimNao:[
      {text: 'Sim', value: 'S'},
      {text: 'Não', value: 'N'},
    ]

  }),

  created(){
    this.reset()
  },

  methods: {
    openDialog(form){
      this.reset()
      if (!form){
        this.titulo = "Incluir Situação de Leito"
        this.reset()
      } else {
        this.titulo = "Alterar Situação de Leito"
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

      let uri = petra.base_uri+"/situacao_leito";
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
        descricao : null,
        disponivel : 'S'
      },
      this.errors = []
    },

    getErrors(fieldName){
      return petra.getErrors(fieldName, this.errors)
    }

  }
}
</script>