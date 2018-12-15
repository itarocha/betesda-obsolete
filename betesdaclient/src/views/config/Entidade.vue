<template>
  <div>
    <dialogo-confirmacao ref="dlgExclusao" :mensagem="descricaoItemExclusao" titulo="Confirmação" @ok="onDelete"></dialogo-confirmacao>
    <entidade-edit ref="dlgEdit" @save="onSave"></entidade-edit>

    <v-layout row wrap>
      <v-flex xs12 sm12 md12>
        <v-btn dark small color="cyan darken-4" @click="incluir">
            Incluir
        </v-btn>
      </v-flex>

      <v-flex xs12 sm12 md12>
        <v-card flat class="ml-2 mr-2">
          <v-data-table :headers="headers" :items="dados" :dark="false" :persistent="true"
          :rows-per-page-items="rowsperpage" :rows-per-page-text="'Linhas por página'"
          class="elevation-1">
            <template slot="items" slot-scope="props">
                <td class="text-xs-right">{{ props.item.id }}</td>
                <td class="text-xs-left">{{ props.item.nome }}</td>
                <td class="text-xs-left">{{ props.item.cnpj }}</td>
                <td class="text-xs-left">{{ props.item.telefone }}</td>
                <td class="text-xs-left">{{ props.item.endereco.descricao }}</td>
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
import EntidadeEdit from './EntidadeEdit.vue'

export default {
  name: 'Entidade',
  components:{
    DialogoConfirmacao,
    EntidadeEdit
  },

  data: () =>({
    dados: [],

    form : {},

    headers: [
      { text: 'Código', align: 'left', value: 'id', width: '5%',  sortable: false,},
      { text: 'Nome', value: 'nome', align:'left', width: '20%' },
      { text: 'CNPJ', value: 'cnpj', align:'left', width: '10%' },
      { text: 'Telefone', value: 'telefone', align:'left', width: '10%' },
      { text: 'Endereço', value: 'endereco.descricao', align:'left', width: '30%' },
      { text: 'Ações', width: '10%', sortable: false}
    ],

    rowsperpage: [10,20,30,{"text":"Todos","value":-1}],
  }),

  created(){
    this.getData()
  },

  mounted(){
    this.$store.dispatch('setAcao','Entidades')
  },

  computed: {
    descricaoItemExclusao(){
      var nome = this.form.nome ? this.form.nome : 'Não selecionado';
      return 'Deseja realmente excluir "'+nome+'"?'
    }
    
  },

  methods: {
      getData(evt) {
        let uri = petra.base_uri+"/app/entidades"; 
        console.log("ENTIDADES AXIOS.HEADER = ",axios.defaults.headers.common)
        axios.get(uri).then(response => {
          this.dados = response.data;
        }).catch(error => {
          console.log("ERRO:",error)
          //reject(error)
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
        this.getData()
      },

      deleteItemConfirm (item) {
        this.form = Object.assign({}, item)
        this.$refs.dlgExclusao.openDialog()
      },

      onDelete(evt) {
        let uri = petra.base_uri+"/app/entidades/"+this.form.id;
        axios.delete(uri)
            .then(response => { 
              this.$store.dispatch('setAcao','')
              this.getData()
            }).catch(error => {
               petra.tratarErros(error); 
            });
      },
  }
}
</script>