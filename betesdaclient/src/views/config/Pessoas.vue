<template>
  <div>
    <dialogo-confirmacao ref="dlgExclusao" titulo="Confirmação" @ok="onDelete"></dialogo-confirmacao>
    <pessoa-edit ref="dlgEdit" @save="onSave"></pessoa-edit>

    <v-layout row wrap>
      <v-flex xs12 sm12 md12>
        <v-btn dark small color="cyan darken-4" @click="incluir">
            Incluir
        </v-btn>
      </v-flex>

      <v-flex xs12 sm12>
        <v-text-field
          ref="editBusca"
          v-model="searchText"
          label="Localizar"
          append-icon="search"
          single-line
          class="input-group--focused"
        ></v-text-field>
      </v-flex>

      <v-flex xs12 sm12 md12 class="ml-2 mr-2">
        <v-card flat>
          <v-data-table :headers="headers" :items="dados" :dark="false" :persistent="true"
          :rows-per-page-items="rowsperpage" :rows-per-page-text="'Linhas por página'"
          class="elevation-1">
            <template slot="items" slot-scope="props">
                <td class="text-xs-right">{{ props.item.id }}</td>
                <td class="text-xs-left">{{ props.item.nome }}</td>
                <td class="text-xs-left">{{ formatDate(props.item.dataNascimento) }}</td>
                <td class="text-xs-left">{{ props.item.endereco.descricao }}</td>
                <td class="text-xs-left">{{ props.item.naturalidadeCidade }}</td>
                <td class="text-xs-left">{{ props.item.naturalidadeUf }}</td>
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
import PessoaEdit from './PessoaEdit.vue'
import Titulo from '../../components/Titulo.vue'

export default {
  name: 'Pessoas',
  components:{
    Titulo,
    DialogoConfirmacao,
    PessoaEdit
  },

  created(){
    this.getData()
  },

  mounted(){
    this.$store.dispatch('setAcao','Pessoas')
  },
  
  data: () =>({
    dados: [],

    searchText : '',

    form : {},

    headers: [
      { text: 'Código', align: 'left', value: 'id', width: '5%',  sortable: false,},
      { text: 'Nome', value: 'nome', align:'left', width: '20%' },
      { text: 'Nascimento', value: 'nascimento', align:'left', width: '10%' },
      { text: 'Endereço', value: 'endereco', align:'left', width: '30%' },
      { text: 'Naturalidade', value: 'naturalidade', align:'left', width: '10%' },
      { text: 'UF', value: 'naturalidadeUf', align:'left', width: '5%' },
      { text: 'Ações', width: '30%', sortable: false}
    ],

    rowsperpage: [10,20,30,{"text":"Todos","value":-1}],
  }),

  watch:{
    searchText : _.debounce( function(){
      if (this.searchText == '') {
        this.dados = []
        return
      }
      petra.axiosGet("/app/pessoas/consultar/" +this.searchText).then(
        response => {
           this.dados = response.data
        })
    },500)
  },

  methods: {
    
    getData(evt) {
      petra.axiosGet("/app/pessoas/consultar/" +this.searchText).then(
        response => {
           this.dados = response.data
        })
    },

    incluir() {
      this.$refs.dlgEdit.openDialog()
    },

    editar(item) {
      this.form = Object.assign({}, item)
      this.$refs.dlgEdit.openDialog(this.form)
    },

    onSave(data){
      petra.showMessageSuccess('Pessoa gravada com sucesso')
      this.getData()
    },

    deleteItemConfirm (item) {
      this.$refs.dlgExclusao.openDialog(`Deseja realmente excluir "${item.nome}"?`)
    },

    onDelete(evt) {
      petra.axiosDelete("/app/pessoas/"+this.form.id)
        .then(response => {
          petra.showMessageSuccess('Pessoa excluída com sucesso')
          this.getData()
        })
        .catch(error => {
          petra.tratarErros(error)
        })
    },

    formatDate(data,formato){
      return petraDateTime.formatDate(data,formato)
    },

    onChangeText: _.debounce((evt) => {
      petra.axiosGet("/app/pessoas/consultar/" +evt.target.value).then(
        response => {
           this.dados = response.data
        })
    }, 500),

  }
}
</script>