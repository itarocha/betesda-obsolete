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
                  <h3>{{nome}}</h3>
              </v-flex>
              <v-flex xs12 sm12 md12>
                <v-select v-model="tipoHospede" ref="selTipoHospede" :items="itensTipoHospede" label="Tipo de Hóspede" required 
                
                :menu-props="{closeOnClick: false, closeOnContentClick: false, openOnClick: false, maxHeight: 300 }"
                ></v-select>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions class="grey lighten-4"> 
          <v-spacer></v-spacer>
          <v-btn small class="white--text cyan darken-4" @click.native="close(true)" :disabled="tipoHospede == null">
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
  name: 'DialogoSelecaoTipoHospede',
  
  props: {
    valores : {
      type: Array,
      default: () => []
    } 
  },

  data: () =>({
    titulo : "Selecionar Tipo de Hóspede",
    nome : String,
    tipoHospede : null,
    dialogVisible : false,
  }),

  created(){
    this.reset()
  },

  computed : {
    itensTipoHospede() {
      if (this.valores){
        return this.valores
      } else {
        return [
          {
            text: 'Xis', value: 'Xiso'
          },
          {
            text: 'Ypsonel', value: 'Ypsloneyou'
          }
        ]
      }
    }
  },

  methods: {
    openDialog(nome){
      this.reset()
      this.nome = nome
      this.dialogVisible = true

      if (this.valores.length > 0) {
        this.tipoHospede = this.valores[0].value
      }

      setTimeout(() => {
          this.$refs.selTipoHospede.focus()
      }, 500)
    },

    close(selected){
      if (selected){
        var item = null
        for (var i = 0; i < this.valores.length; i++) {
          if (this.tipoHospede == this.valores[i].value){
            item = {
                    id : this.valores[i].value, 
                    descricao : this.valores[i].text
                  }
            break;
          }
        }

        this.$emit('close',item)
      } else {
        this.$emit('close',null)
      }
      this.dialogVisible = false
    },

    reset(){
      this.tipoHospede = null
    },

  }
}
</script>