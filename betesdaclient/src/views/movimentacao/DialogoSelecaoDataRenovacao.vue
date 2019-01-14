<template>
  <div>
    <v-dialog v-model="dialogVisible" width="400">
        <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          Selecionar Data para Renovação da Hospedagem #{{hospedagemId}}
        </v-card-title>
        <v-card-text>
          <v-container grid-list-md>
            <v-layout wrap>
              <v-flex xs12 sm12 md12>
                  <v-menu
                      ref="menu1"
                      :close-on-content-click="false"
                      v-model="menu1"
                      :nudge-right="40"
                      lazy
                      transition="scale-transition"
                      offset-y
                      full-width
                      max-width="290px"
                      min-width="290px"
                    >
                      <v-text-field
                        slot="activator"
                        ref="edtDataRenovacao"
                        v-model="dataRenovacaoFmt"
                        label="Data de Renovação"
                        persistent-hint
                        prepend-icon="event"
                        v-mask="'##/##/####'"
                        @change="dataRenovacaoBlur"
                      ></v-text-field>
                      <v-date-picker v-model="dataRenovacao" locale="pt-br" no-title @input="menu1 = false"></v-date-picker>
                  </v-menu>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions class="grey lighten-4"> 
          <v-spacer></v-spacer>
          <v-btn small class="white--text cyan darken-4" @click.native="close(true)" :disabled="dataRenovacaoFmt == null">
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
import {mask} from "vue-the-mask"

export default {
  name: 'DialogoSelecaoDataRenovacao',

  directives:{
    mask
  },

  props: {
  },

  data: () =>({
    hospedagemId: null,
    dataRenovacao: null,
    dataRenovacaoFmt: null,
    dialogVisible : false,
    menu1: false,
  }),

  created(){
    this.reset()
  },

  computed : {
  },

  watch : {
    dataRenovacao (val) {
      this.dataRenovacaoFmt = this.formatDate(this.dataRenovacao)
    },    
  },

  methods: {
    openDialog(hospedagemId, dataPrevistaSaida){
      this.reset()
      this.hospedagemId = hospedagemId
      this.dataRenovacao = dataPrevistaSaida
      this.dataRenovacaoFmt = this.formatDate(this.dataRenovacao)
      this.dialogVisible = true
      setTimeout(() => {
          this.$refs.edtDataRenovacao.focus()
      }, 500)
    },
    
    dataRenovacaoBlur(){
      this.dataRenovacao = this.parseDate(this.dataRenovacaoFmt)
    },

    formatDate(date) {
      if (!date) return null

      const [year, month, day] = date.split('-')
      return `${day}/${month}/${year}`
    },
    
    parseDate(date) {
      if (!date) return null

      const [day, month, year] = date.split('/')
      var retorno = `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
      return retorno
    },    

    close(status){
      if (status){
        this.$emit('close',this.hospedagemId, this.dataRenovacao)
      } else {
        this.$emit('close',this.hospedagemId, null)
      }
      this.dialogVisible = false
    },

    reset(){
      this.dataRenovacao = null
      this.dataRenovacaoFmt = null
    },

  }
}
</script>