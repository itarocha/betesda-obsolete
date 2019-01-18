<template>
  <div>
    <dialogo-confirmacao ref="dlgExclusao" titulo="Confirmação" @ok="onDelete"></dialogo-confirmacao>
    <entidade-edit ref="dlgEditEntidade" @save="onSaveEntidade"></entidade-edit>
    <encaminhador-edit ref="dlgEditEncaminhador" @save="onSaveEncaminhador"></encaminhador-edit>

    <v-layout row wrap>
      <v-flex xs12 sm12 md12>
        <v-btn dark small color="cyan darken-4" @click="incluir">
            {{tituloBotaoIncluir}}
        </v-btn>
        <v-btn dark small color="cyan darken-4" @click="gerenciarEntidades" v-if="mode=='encaminhadores'">
            Voltar para Entidades
        </v-btn>
      </v-flex>

      <v-flex xs12 sm12 md12 v-if="mode == 'entidades'">
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
                  <v-icon small class="mr-2" @click="editarEntidade(props.item)">edit</v-icon>
                  <v-icon small class="mr-2" @click="deleteItemConfirm(props.item)">delete</v-icon>
                  <v-icon small color="red darken-4" @click="gerenciarEncaminhadores(props.item)">fa-user</v-icon>
                </td>
            </template>
          </v-data-table>    
        </v-card>
      </v-flex>

      <v-flex xs12 sm12 md12 v-if="mode == 'encaminhadores'">
        <h2 class="ml-2 mr-2">{{formEntidade.nome}}</h2>
      </v-flex>  

      <v-flex xs12 sm12 md12 v-if="mode == 'encaminhadores'">
        <v-card flat class="ml-2 mr-2">
          <v-data-table :headers="headers" :items="encaminhadores" :dark="false" :persistent="true"
          :rows-per-page-items="rowsperpage" :rows-per-page-text="'Linhas por página'"
          class="elevation-1">
            <template slot="items" slot-scope="props">
                <td class="text-xs-right">{{ props.item.id }}</td>
                <td class="text-xs-left">{{ props.item.nome }}</td>
                <td class="text-xs-left">{{ props.item.cargo }}</td>
                <td class="text-xs-left">{{ props.item.telefone }}</td>
                <td class="text-xs-left">{{ props.item.email }}</td>
                <td class="text-xs-left">
                  <v-icon small class="mr-2" @click="editarEncaminhador(props.item)">edit</v-icon>
                  <v-icon small @click="deleteEncaminhadorConfirm(props.item)">delete</v-icon>
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
import EncaminhadorEdit from './EncaminhadorEdit.vue'

export default {
  name: 'Entidade',
  components:{
    DialogoConfirmacao,
    EntidadeEdit,
    EncaminhadorEdit,
  },

  data: () =>({
    dados: [],
    encaminhadores: [],
    mode: 'entidades',
    idToDelete : null,

    form : {},
    formEntidade : {},

    headers: [
      { text: 'Código', align: 'left', value: 'id', width: '5%',  sortable: false,},
      { text: 'Nome', value: 'nome', align:'left', width: '20%' },
      { text: 'CNPJ', value: 'cnpj', align:'left', width: '10%' },
      { text: 'Telefone', value: 'telefone', align:'left', width: '10%' },
      { text: 'Endereço', value: 'endereco.descricao', align:'left', width: '30%' },
      { text: 'Ações', width: '10%', sortable: false}
    ],

    headersEncaminhadores: [
      { text: 'Código', align: 'left', value: 'id', width: '5%',  sortable: false,},
      { text: 'Nome', value: 'nome', align:'left', width: '20%' },
      { text: 'Cargo', value: 'cargo', align:'left', width: '10%' },
      { text: 'Telefone', value: 'telefone', align:'left', width: '10%' },
      { text: 'Email', value: 'email', align:'left', width: '10%' },
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

    tituloBotaoIncluir(){
      return this.mode == 'entidades' ? 'Incluir Entidade' : 'Incluir Encaminhador'
    }
  },

  methods: {

    getData(evt) {
      petra.axiosGet("/app/entidades").then(
        response => this.dados = response.data
      )
    },

    incluir() {
      if (this.mode == 'entidades') {
        this.$refs.dlgEditEntidade.openDialog()
      } else {
        var form = {entidade : this.formEntidade}                    

        this.$refs.dlgEditEncaminhador.openDialog(form)
      }
    },

    editarEntidade(item) {
      this.form = Object.assign({}, item)
      this.$refs.dlgEditEntidade.openDialog(this.form)
    },

    editarEncaminhador(item) {
      var form = Object.assign({}, item)
      form.entidade = this.formEntidade                       
      this.$refs.dlgEditEncaminhador.openDialog(form)
    },

    gerenciarEncaminhadores(item) {
      this.formEntidade = item
      this.getEncaminhadores(item.id)
      this.mode='encaminhadores'
    },

    getEncaminhadores(entidadeId) {
      petra.axiosGet("/app/encaminhadores/por_encaminhador/"+entidadeId).then(
        response => {
          this.encaminhadores = response.data
        } 
      )
    },

    gerenciarEntidades() {
      this.form = null
      this.mode='entidades'
    },

    onSaveEntidade(data){
      petra.showMessageSuccess('Entidade gravada com sucesso')
      this.getData()
    },

    onSaveEncaminhador(data){
      petra.showMessageSuccess('Encaminhador gravado com sucesso')
      this.getEncaminhadores(this.formEntidade.id)
    },

    deleteItemConfirm(item) {
      this.idToDelete = item.id
      var mensagem = 'Deseja realmente excluir "'+item.nome+'"?'
      this.$refs.dlgExclusao.openDialog(mensagem)
    },

    deleteEncaminhadorConfirm (item) {
      this.idToDelete = item.id
      var mensagem = 'Deseja realmente excluir "'+item.nome+'"?'
      this.$refs.dlgExclusao.openDialog(mensagem)
    },

    onDelete(evt) {
      if (this.mode == 'entidades') {
        petra.axiosDelete("/app/entidades/"+this.idToDelete)
          .then(response => {
            petra.showMessageSuccess('Entidade excluída com sucesso')
            this.getData()
          })
          .catch(error => {
            petra.tratarErros(error)
          })
      } else {
        petra.axiosDelete("/app/encaminhadores/"+this.idToDelete)
          .then(response => {
            petra.showMessageSuccess('Encaminhador excluído com sucesso')
            this.getEncaminhadores(this.formEntidade.id)
          })
          .catch(error => {
            petra.tratarErros(error)
          })
      }
    },
  }
}
</script>