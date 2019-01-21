<template>
  <div>
    <dialogo-confirmacao ref="dlgExclusaoQuarto" titulo="Confirmação" @ok="onDeleteQuarto"></dialogo-confirmacao>
    <dialogo-confirmacao ref="dlgExclusaoLeito" titulo="Confirmação" @ok="onDeleteLeito"></dialogo-confirmacao>
    <quarto-insert ref="dlgQuartoInsert" @save="onSaveQuarto"></quarto-insert>
    <quarto-edit ref="dlgQuartoEdit" @save="onSaveQuarto"></quarto-edit>
    <quarto-leito-edit ref="dlgQuartoLeitoEdit" @save="onSaveLeito"></quarto-leito-edit>

    <v-layout row wrap>
      <v-flex xs12 sm12 md12>
        <v-btn dark small color="cyan darken-4" @click="incluirQuarto">
            Incluir
        </v-btn>
      </v-flex>

      <v-flex xs12 sm12 md12>
        <!-- color="teal darken-3" dark slider-color="yellow" -->
        <v-tabs fixed-tabs v-model="active" v-on:input="inputTabEvent" slider-color="cyan darken-2">
          <v-tab v-for="(quarto, index)  in dados" :key="index" ripple>
            Quarto {{ quarto.numero }}
          </v-tab>

          <v-tab-item v-for="(quarto, index) in dados" :key="index" ripple>
            <v-layout row wrap class="subheading pa-2">
              <v-flex xs12 class="subheading ml-2">
                  Destinações de Hospedagem:
                  <v-chip color="amber lighten-2" v-for="(destinacaoHospedagem, idx) in quarto.destinacoes" :key="idx">{{destinacaoHospedagem.descricao}}</v-chip>
              </v-flex>

              <v-flex xs12>
                <v-btn small color="cyan darken-4" class="white--text" @click="incluirLeito(quarto)">Novo Leito<v-icon right dark>add</v-icon></v-btn>
                <v-btn small color="cyan darken-4" class="white--text" @click="editItem(quarto)">Alterar Quarto<v-icon right dark>edit</v-icon></v-btn>
                <v-btn small color="cyan darken-4" class="white--text" @click="deleteItem(quarto)">Excluir Quarto<v-icon right dark>delete</v-icon></v-btn>
              </v-flex>

              <v-container fluid grid-list-lg>
                <v-layout row wrap>
                  <v-flex xs3 v-for="(leito, i)  in dados[index].leitos" :key="i" >
                    <v-card color="amber lighten-4">
                      <h2 class="headline ml-2 mt-2">{{leito.numero}}</h2>  
                      <v-card-text class="body-1 mb-0">
                        <div>{{leito.tipoLeito.descricao}} - {{leito.tipoLeito.quantidadeCamas}} cama(s)</div>
                        <div>{{leito.situacao.descricao}}</div>
                      </v-card-text>

                      <v-divider light></v-divider>
                      <v-card-actions class="grey lighten-4">
                        <v-spacer></v-spacer>
                        <v-icon @click="editLeito(quarto, leito)">edit</v-icon>
                        <v-icon @click="deleteLeito(leito)">delete</v-icon>
                      </v-card-actions>
                    </v-card>
                  </v-flex>
                </v-layout>
              </v-container>

            </v-layout>
          </v-tab-item>
        </v-tabs>

      </v-flex>
    </v-layout>

  </div>
</template>

<script>
 
import DialogoConfirmacao from './DialogoConfirmacao.vue'
import QuartoInsert from "./QuartoInsert.vue"
import QuartoEdit from "./QuartoEdit.vue"
import QuartoLeitoEdit from "./QuartoLeitoEdit.vue"
import Titulo from "../../components/Titulo.vue"

export default {
  name: "Quarto",
  components:{
    Titulo,
    DialogoConfirmacao,
    QuartoInsert,
    QuartoEdit,
    QuartoLeitoEdit,
  },
  data: () => ({
    dados: [],
    leitos: [],
    
    descricaoExclusaoQuarto : "",
    descricaoExclusaoLeito : "",

    quarto_numero: 1,
    itensDestinacaoHospedagem: [],
    itensTipoLeito: [],
    itensSituacaoLeito: [],

    active: null,

    pagination: {
      rowsPerPage: 10
    },

    quartoSelecionado: {},

    formQuarto: {
      numero: 0,
      descricao: null,
      destinacoes: null,
      quantidadeLeitos: 0,
      tipoLeito: null,
      situacao: null
    },

    formLeito: {
      numero: 0
    },
  }),

  created() {
    this.getData()
  },

  mounted(){
    this.$store.dispatch('setAcao','Quartos')
  },

  methods: {

    loadListas() {
      this.itensDestinacaoHospedagem = []
      petra.axiosGet("/app/quarto/listas").then(
        response => {
          this.itensDestinacaoHospedagem = response.data.listaDestinacaoHospedagem
          this.itensTipoLeito = response.data.listaTipoLeito
          this.itensSituacaoLeito = response.data.listaSituacaoLeito
        })
    },

    getData(evt) {
      petra.axiosGet("/app/quarto").then(
        response => {
          this.dados = response.data;
          if ((!this.active) &&  (this.dados.length > 0)){
            this.active = 0;
            this.inputTabEvent(this.active);
          } else if ((this.active) && (this.active >= 0) && (this.dados.length > 0)) {
            this.inputTabEvent(this.active);
          }
        })
    },

    onSaveQuarto(data){
      this.getData()
    },

    incluirQuarto(evt) {
       this.$refs.dlgQuartoInsert.openDialog()
    },

    incluirLeito(quarto) {
      var form = {
        id: null,
        quartoId: quarto.id,
        numero: 0,
        tipoLeito: null,
        situacao: null
      };

      this.$refs.dlgQuartoLeitoEdit.openDialog(form, quarto)
    },

    editItem(item) {
      var destinacoes = []
      for(var i = 0; i < item.destinacoes.length; i++){
        destinacoes.push(item.destinacoes[i].id)
      }
      var form = {
        id: item.id,
        numero: item.numero,
        descricao: item.descricao,
        destinacoes : destinacoes
      }

      this.$refs.dlgQuartoEdit.openDialog(form)
    },

    editLeito(quarto, leito) {
      var form = {
        id: leito.id,
        quartoId: quarto.id,
        numero: leito.numero,
        tipoLeito: leito.tipoLeito.id,
        situacao: leito.situacao.id
      };
      this.$refs.dlgQuartoLeitoEdit.openDialog(form, quarto)
    },

    onSaveLeito(data){
      this.getData()
    },

    inputTabEvent(event){
      const ativo = parseInt(event);

      if (this.dados.length > 0) {
        this.quartoSelecionado = this.dados[ativo]
      } else {
        this.quartoSelecionado = {}
      }
    },

    setQuartoSelecionado(quarto){
      this.quartoSelecionado = quarto
    },

    deleteItem(item) {
      this.formQuarto = Object.assign({}, item);
      this.$refs.dlgExclusaoQuarto.openDialog(`Deseja realmente excluir o Quarto "${item.numero}"?`)
    },

    onDeleteQuarto(evt) {
      this.errors = [];
      petra.axiosDelete("/app/quarto/"+this.formQuarto.id)
        .then(response => {
          petra.showMessageSuccess('Quarto excluído com sucesso')
          this.getData()
        })
        .catch(error => {
          petra.tratarErros(error)
        })
    },

    deleteLeito(leito) {
      this.formLeito = Object.assign({}, leito);
      this.$refs.dlgExclusaoLeito.openDialog(`Deseja realmente excluir o Leito "${leito.numero}"?`)
    },

    onDeleteLeito(evt) {
      this.errors = [];

      petra.axiosDelete("/app/quarto/leito/"+this.formLeito.id)
        .then(response => {
          petra.showMessageSuccess('Quarto excluído com sucesso')
          this.getData()
        })
        .catch(error => {
          petra.tratarErros(error)
        })
    },

    cancelar() {
      this.getData();
    },

    reset(evt) {
      (this.formQuarto = {
        numero: 0,
        descricao: null,
        destinacoes: null,
        quantidadeLeitos: 0,
        tipoLeito: null,
        situacao: null
      }),
        (this.errors = []);
    },

  }
};
</script>