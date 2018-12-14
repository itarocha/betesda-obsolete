<template>
  <div>

    <dialogo-selecao-tipo-hospede ref="dlgSelecaoTipoHospede" :valores="itensSelecaoTipoHospede" @close="onCloseSelecaoTipoHospede"></dialogo-selecao-tipo-hospede>
    <pessoa-edit ref="dlgPessoaEdit" @save="onUpdatePessoa"></pessoa-edit>

    <v-dialog v-model="dialogVisible" width="600" :scrollable="true">

      <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          Selecionar Pessoa
        </v-card-title>
        <v-card-text  style="height: 500px;">
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

            <v-card>
              <v-card-text>
                <v-data-table :headers="headers" :items="dados" hide-actions :dark="false" :persistent="true" class="elevation-0">
                  <template slot="items" slot-scope="props">
                      <td class="text-sm-right">{{ props.item.id }}</td>
                      <td class="text-sm-left">{{ props.item.nome }}</td>
                      <td class="text-sm-center">
                        <v-icon class="mr-2" @click="selecionarPessoa(props.item)">check_circle</v-icon>
                      </td>
                  </template>
                </v-data-table>    
              </v-card-text>
            </v-card>

        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions class="grey lighten-4"> 
          <v-spacer></v-spacer>
          <v-btn small dark color="cyan darken-4" @click.native="novo">
            Incluir Pessoa
          </v-btn>
          <v-btn small dark color="cyan darken-4" @click.native="close(true)">
            Fechar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>

import DialogoSelecaoTipoHospede from './DialogoSelecaoTipoHospede.vue'
import PessoaEdit from './PessoaEdit.vue'
import _ from 'lodash'

export default {
  name: 'DialogoSelecaoPessoa',

  components : {
    DialogoSelecaoTipoHospede,
    PessoaEdit
  },
  
  props: {
  },

  data: () =>({
    dialogVisible : false,
    pessoa : null,
    searchText : '',
    dados : [],
    itensTipoHospede : [],
    headers: [
      { text: 'Código', align: 'left', value: 'id', width: '10%',  sortable: false,},
      { text: 'Nome', value: 'nome', align:'left', width: '50%' },
      { text: 'Ações', align: 'center', width: '30%', sortable: false}
    ],
    rowsperpage: [10,20,30,{"text":"Todos","value":-1}],
  }),

  watch:{
    searchText : _.debounce( function(){
      if (this.searchText == '') {
        this.dados = []
        return
      }
      let uri = petra.base_uri+"/pessoas/consultar/"+this.searchText; 
      axios.get(uri).then(response => {
        this.dados = response.data;
      });        
    },500)
  },

  computed: {
    itensSelecaoTipoHospede(){
      return this.itensTipoHospede
    },
  },

  created(){
  },

  mounted(){
  },

  methods: {
    // publico
    openDialog(){
      this.dialogVisible = true
      this.pessoa = null,
      this.loadListas()

      setTimeout(() => {
          this.$refs.editBusca.focus()
      }, 500)
    },

    loadListas(evt) {
      this.itensDestinacaoHospedagem = [];
      let uri = petra.base_uri + "/quarto/listas";
      axios.get(uri).then(response => {
        this.itensDestinacaoHospedagem = response.data.listaDestinacaoHospedagem;
        this.itensTipoLeito = response.data.listaTipoLeito;
        this.itensSituacaoLeito = response.data.listaSituacaoLeito;
        this.itensTipoHospede = response.data.listaTipoHospede;
      });
    },

    onChangeText: _.debounce((e) => {
        let uri = petra.base_uri+"/pessoas/consultar/" +e.target.value
        
        axios.get(uri).then(response => {
          this.dados = response.data;
        });      

    }, 500),

    close(value){
      this.dialogVisible = false
      this.searchText = ""
      this.$emit('close', value)
    },

    selecionarPessoa(_pessoa){
      this.pessoa = _pessoa;
      this.$refs.dlgSelecaoTipoHospede.openDialog(this.pessoa.nome)
    },

    onUpdatePessoa(pessoa){
      //
    },

    novo(){
      this.$refs.dlgPessoaEdit.openDialog()
    },

    onCloseSelecaoTipoHospede(tipoHospede){
      if (tipoHospede) {
        this.$emit('selecionar', this.pessoa, tipoHospede)
      }

      setTimeout(() => {
          this.$refs.editBusca.focus()
      }, 500)
    }
  }
}
</script>