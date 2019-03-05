<template>
  <div>

    <el-dialog title="Selecionar Leito" :visible.sync="dialogVisible" width="750px">
      <el-row type="flex">
        <el-col :span="18">
          <h4>{{nomeHospede}}</h4>
        </el-col>
        <el-col :span="6">
          <h4 v-if="acomodacao != null">Seleção: Quarto {{acomodacao.quarto.numero}} Leito {{acomodacao.leito.numero}}</h4>
        </el-col>
      </el-row>
      <el-row type="flex">
        <el-col :sm="24" :md="24" :lg="24">
          <el-tabs type="border-card" ref="tab" v-model="activeTabName" @tab-click="handleTabClick">
            <el-tab-pane v-for="(quarto, index) in itensQuarto" :key="index" :label="'Quarto ' + quarto.numero" :name="getNomeTab(quarto)">

              <div class="flex-container wrap">
                <el-card :class="classeSituacaoLeito(leito)" v-for="(leito, i) in leitos" :key="i" 
                    shadow="never" style="width:200px;" @click.native="selecionarLeito(quarto, leito)" body-style="bodystyle">
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
        <el-button type="primary" :disabled="acomodacao == null" @click="doSelecionarLeito">Confirmar</el-button>
      </span>
    </el-dialog>

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

    acomodacao : null,


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
      var quarto = this.quartos[tab.index]
      if (quarto){
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
      return this.acomodacao && id == this.acomodacao.leito.id
    },

    classeSituacaoLeito(leito){
      var id = leito ? leito.id : -1

      var leitoSelecionado = this.leitoSelecionado(id)
      var leitoOcupado = this.isLeitoOcupado(id)
      return leitoSelecionado ? "flex-item leito-selecionado" : leitoOcupado ? "flex-item leito-ocupado" : "flex-item  leito-livre"
    },

    isLeitoOcupado(id){
      return (this.leitosOcupados.indexOf(id) >= 0);
    },

    selecionarLeito(quarto, leito){
      if (this.isLeitoOcupado(leito.id)){
        // mensagem
      } else {
        this.acomodacao = {quarto: {id: quarto.id, numero: quarto.numero}, leito: {id: leito.id, numero: leito.numero}}
      }

    },

    doSelecionarLeito(){
        this.$emit('onSelecionar', this.acomodacao)
        this.dialogVisible = false
    },

    // publico
    getSelecao(){
      var item = null
      
      for (var i = 0; i < this.quartos.length; i++) {
        if (this.quarto == this.quartos[i].id){
          item = {
            hospede : this.hospede,
            acomodacao : this.acomodacao
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
      this.acomodacao = null
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
  /*background: #FFF9C4;*/
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

.leito-selecionado {
  background: #FFE57F;
}

.leito-ocupado {
  background: #D32F2F;
  color: white;
}

.leito-livre {
  background:whitesmoke;
}


</style>

