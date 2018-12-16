<template>
  <div>
    <dialogo-confirmacao ref="dlgExclusao" :mensagem="descricaoItemExclusao" titulo="Confirmação" @ok="onDelete"></dialogo-confirmacao>
    <destinacao-hospedagem-edit ref="dlgEdit" @save="onSave"></destinacao-hospedagem-edit>

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
import DestinacaoHospedagemEdit from './DestinacaoHospedagemEdit.vue'
import Titulo from '../../components/Titulo.vue'

export default {
  name: 'DestinacaoHospedagem',
  components:{
    Titulo,
    DialogoConfirmacao,
    DestinacaoHospedagemEdit
  },
  data: () =>({
    dados: [],

    form : {},

    headers: [
      { text: 'Código', align: 'left', value: 'id', width: '10%',  sortable: false,},
      { text: 'Descrição', value: 'descricao', align:'left', width: '50%' },
      { text: 'Ações', width: '30%', sortable: false}
    ],

    rowsperpage: [10,20,30,{"text":"Todos","value":-1}],
  }),

  computed: {

    descricaoItemExclusao(){
      var descricao = this.form.descricao ? this.form.descricao : 'Não selecionado';
      return 'Deseja realmente excluir "'+descricao+'"?'
    }
    
  },

  created(){
    this.getData()
  },

  mounted(){
    this.$store.dispatch('setAcao','Destinações de Hospedagem')
  },

  methods: {

    getData(evt) {
      petra.axiosGet("/app/destinacao_hospedagem").then(
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
      petra.showMessageSuccess('Destinação de Hospedagem gravada com sucesso')
      this.getData()
    },

    deleteItemConfirm (item) {
      this.form = Object.assign({}, item)
      this.$refs.dlgExclusao.openDialog()
    },

    onDelete(evt) {
      petra.axiosDelete("/app/destinacao_hospedagem/"+this.form.id)
        .then(response => {
          petra.showMessageSuccess('Destinação de Hospedagem excluída com sucesso')
          this.getData()
        })
        .catch(error => {
          petra.tratarErros(error)
        })
    },
  }
} 
</script>