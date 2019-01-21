<template>
  <div>
    <dialogo-confirmacao ref="dlgExclusao" titulo="Confirmação" @ok="onDelete"></dialogo-confirmacao>
    <tipo-leito-edit ref="dlgEdit" @save="onSave"></tipo-leito-edit>

    <v-layout row wrap>
      <v-flex xs12 sm12 md12>
        <v-btn dark small color="cyan darken-4" @click="incluir">
            Incluir
        </v-btn>
      </v-flex>

      <v-flex xs12 sm12 md12 class="ml-2 mr-2">
        <v-card flat>
          <v-data-table :headers="headers" :items="dados" :dark="false" :persistent="true"
          :rows-per-page-items="rowsperpage" :rows-per-page-text="'Linhas por página'"
          class="elevation-1">
            <template slot="items" slot-scope="props">
                <td class="text-xs-right">{{ props.item.id }}</td>
                <td class="text-xs-left">{{ props.item.descricao }}</td>
                <td class="text-xs-left">{{ props.item.quantidadeCamas }}</td>
                <td class="text-xs-left">
                  <v-icon small class="mr-2" @click="editar(props.item)">edit</v-icon>
                  <v-icon small @click="deleteItemConfirm(props.item)">delete</v-icon>
                </td>
            </template>
          </v-data-table>    
        </v-card>
      </v-flex>
    </v-layout>
  </div>
</template>

<script>

import DialogoConfirmacao from './DialogoConfirmacao.vue'
import TipoLeitoEdit from './TipoLeitoEdit.vue'
import Titulo from '../../components/Titulo.vue'

export default {
  name: 'TipoLeito',
  components:{
    Titulo,
    DialogoConfirmacao,
    TipoLeitoEdit
  },

  data: () =>({
    dados: [],

    form : {},

    headers: [
      { text: 'Código', align: 'left', value: 'id', width: '10%',  sortable: false,},
      { text: 'Descrição', value: 'descricao', align:'left', width: '50%' },
      { text: 'Qtd. Camas', value: 'quantidadeCamas', align:'left', width:'10%'},
      { text: 'Ações', width: '30%', sortable: false}
    ],

    rowsperpage: [10,20,30,{"text":"Todos","value":-1}],
  }),

  created(){
    this.getData()
  },

  mounted(){
    this.$store.dispatch('setAcao','Tipos de Leitos')
  },

  methods: {

    getData(evt) {
      petra.axiosGet("/app/tipo_leito").then(
        response => this.dados = response.data
      )
    },

    incluir() {
      this.$refs.dlgEdit.openDialog()
    },

    editar(item) {
      this.form = Object.assign({}, item)
      this.$refs.dlgEdit.openDialog(this.form)
    },

    onSave(data){
      petra.showMessageSuccess('Tipo de Leito gravado com sucesso')
      this.getData()
    },

    deleteItemConfirm (item) {
      this.$refs.dlgExclusao.openDialog(this.$refs.dlgExclusao.openDialog(  `Deseja realmente excluir o tipo de leito "${item.descricao}"?` ))
    },

    onDelete(evt) {
      petra.axiosDelete("/app/tipo_leito/"+this.form.id, false)
        .then(response => {
          petra.showMessageSuccess('Tipo de Leito excluído com sucesso')
          this.getData()
        })
        .catch(error => {
          petra.tratarErros(error)
        })
    },
  }
}
</script>