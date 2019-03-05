<template>
  <div>

    <el-dialog title="Selecionar Leito" :visible.sync="dialogVisible" width="750px">
        <el-row type="flex">
          <el-col>
            <h4>{{nomeHospede}}</h4>
          </el-col>
        </el-row>
        <el-row type="flex">
          <el-col :sm="24" :md="24" :lg="24">
            <el-tabs type="border-card" ref="tab" v-model="activeTabName" @tab-click="handleTabClick">
              <el-tab-pane v-for="(quarto, index) in itensQuarto" :key="index" :label="'Quarto ' + quarto.numero" :name="getNomeTab(quarto)">

                <div class="flex-container wrap">
                  <!--
                  <div class="flex-item" v-for="(leito, i)  in leitos" :key="i" style="width:200px;" @click.native="selecionarLeito(leito)">
                    <div slot="header" class="clearfix" @click.native="selecionarLeito(leito)">
                      <span class="numero_leito">{{leito.numero}}</span>
                    </div> 
                    <div style="font-size:0.8em">{{leito.tipoLeito.descricao}} - {{leito.tipoLeito.quantidadeCamas}} cama(s)</div>                      
                  </div>
                  -->
                  <el-card class="flex-item" v-for="(leito, i)  in leitos" :key="i" shadow="never" style="width:200px;" @click.native="selecionarLeito(leito)" body-style="bodystyle">
                    <div class="clearfix elcardheader">
                      <span class="numero_leito">{{leito.numero}}</span>
                    </div> 
                    <div style="font-size:0.8em">{{leito.tipoLeito.descricao}} - {{leito.tipoLeito.quantidadeCamas}} cama(s)</div>                      
                  </el-card>
                </div>



              </el-tab-pane>
            </el-tabs>
          </el-col>
        </el-row>    




      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancelar</el-button>
        <el-button type="primary" @click="dialogVisible = false">Confirmar</el-button>
      </span>
    </el-dialog>

    <!--
    <v-layout row wrap>
      <v-flex xs12 sm12 md12>
        <v-select v-model="quarto" :items="itensQuarto" item-text="displayText" item-value="id" label="Quarto" required></v-select>
      </v-flex>
      <v-flex xs12 sm12 md12 v-if="quarto != null">
        <p style="color:'red'">Clique sobre a representação do leito para selecionar...{{dataIni}} - {{dataFim}}</p>
      </v-flex>

      <v-flex xs3 v-for="(leito, i) in leitos" :key="i">
        <v-card :class="classeSituacaoLeito(leito)" @click.native="selecionarLeito(leito)">
          <v-card-text class="text-sm">
            <h3>{{leito.numero}}</h3>
            <div class="caption">{{leito.tipoLeito.descricao}}</div>
            <div class="caption">{{leito.tipoLeito.quantidadeCamas}} cama(s)</div>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
    -->
  </div>
</template>

<script>

export default {
  name: "FrameSelecaoLeito",
  
  dataIni : null,
  dataFim : null,

  data: () =>({
    dialogVisible : false,

    activeTabName: null,

    nomeHospede : "",
    hospede : null,
    quarto : null,
    leito: null,
    leitos : [],
    quartos : [],
    leitosOcupados : []
  }),

  created(){
    this.reset()
  },

  mounted(){
  },

  watch: {
    quarto(value) {
      this.$emit('selecionarQuarto',value)
      this.leito = null

      this.refreshLeitos(value)
    },

    activeTabName(value){
      console.log("activeTabName ",value)
    }
  },

  computed : {
    itensQuarto() {
      if (this.quartos){
        return this.quartos
      } else {
        return []
      }
    }
  },

  methods: {

    teste(){
      this.dialogVisible = true
    },

    openDialog(hospede, destinacaoHospedagemId, dataIni, dataFim){
      this.reset()

      var hoje = petraDateTime.hoje()
      this.dataIni = dataIni || hoje
      this.dataFim = dataFim || hoje

      this.loadLeitosOcupados(() =>{
        this.loadQuartosPorTipoUtilizacao(destinacaoHospedagemId)
      })

      this.hospede = hospede
      this.nomeHospede = hospede.pessoa.nome
      this.dialogVisible = true
    },

    handleTabClick(tab){
      //console.log("clicou em",tab.index)
      var quarto = this.quartos[tab.index]
      if (quarto){
        //console.log("clicou em",tab.index, quarto)
        this.leitos = quarto.leitos
      }
    },

    getNomeTab(quarto){
      var retorno = `quarto${quarto.numero}`
      return retorno
    },

    loadQuartosPorTipoUtilizacao(destinacaoHospedagemId){
      this.quartos = [];
      if (destinacaoHospedagemId != null){
        petra.axiosGet("/app/quarto/por_destinacao_hospedagem/"+destinacaoHospedagemId)
          .then(response => {
            this.quartos = response.data

            if (this.quartos.length > 0){
              var q = this.quartos[0]
              this.leitos = q.leitos
              this.activeTabName = `quarto${q.numero}`
            }
            /*
            if (this.quartos.length > 0){
              this.quarto = this.quartos[0].id
              this.refreshLeitos(this.quarto.id)
            }
            */

          })
      }
    },

    refreshLeitos(quartoId){
      var leitos = []
      for(var i=0; i < this.quartos.length; i++){
        if (this.quartos[i].id == quartoId){
          leitos = this.quartos[i].leitos
          break
        }
      }
      this.leitos = leitos
    },

    leitoSelecionado(id){
      return this.leito && id == this.leito.id
    },

    classeSituacaoLeito(leito){
      var id = leito ? leito.id : -1

      var leitoSelecionado = this.leitoSelecionado(id)
      var leitoOcupado = this.isLeitoOcupado(id)
      return leitoSelecionado ? "amber lighten-4 ma-1" : leitoOcupado ? "red lighten-2 ma-1" : "grey lighten-4 ma-1"
    },

    isLeitoOcupado(id){
      return (this.leitosOcupados.indexOf(id) >= 0);
    },

    selecionarLeito(leito){
      console.log("selecionando leito ", leito)
      if (this.isLeitoOcupado(leito.id)){
        // mensagem
      } else {
        this.leito = leito
        this.$emit('selecionarLeito',leito)
      }

    },

    // publico
    getSelecao(){
      var item = null
      
      for (var i = 0; i < this.quartos.length; i++) {
        if (this.quarto == this.quartos[i].id){
          item = {
            hospede : this.hospede,
            quarto : this.quartos[i], 
            leito : this.leito
          }
          return item
        }
      }
      return null
    },

    loadLeitosOcupados(callback, reject) {

      var request = {
        dataIni : this.dataIni,
        dataFim : this.dataFim
      }

      this.leitosOcupados = []
      petra.axiosPost("/app/hospedagem/leitos_ocupados", request).then(
        response => {
          this.leitosOcupados = response.data
          callback(response)
        })
    },

    reset(){
      this.tipoHospede = null
      this.quarto = null
      this.leito = null
      this.leitos = []
    },

  }  
};
</script>

<style scoped>

.elcardheader{
  padding:3px;
}

 .bodystyle{
   padding:0px;
 } 

.el-card .body {
  padding: 10px;
}
.el-card__head {
  padding: 10px;
}

.flex-container {
  padding: 0;
  margin: 0;
  list-style: none;
  /*border: 1px solid silver;*/
  box-orient: horizontal;
  /*-ms-box-orient: horizontal;*/
  display: flex;
  justify-content: start;
}

.wrap    { 
  flex-wrap: wrap;
}  

.wrap li {
  background: gold;
}

.flex-item {
  border-radius: 4px;
  /*line-height: 1.5em;*/
  /*background: #FFF8E1;*/
  background: #FFF9C4;
  color: #455A64;
  padding: 10px;

  width: 300px;
  /*height: 100px;*/
  margin: 5px;
  
  /*
  line-height: 100px;
  color: white;
  font-weight: bold;
  font-size: 2em;
  text-align: center;
  */
}


</style>

