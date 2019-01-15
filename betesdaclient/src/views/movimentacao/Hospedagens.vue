<template>
  <div>
    <hospedagem-info ref="hospedagemInfo" @save="recarregar" @encerrada="onEncerrada" @close="onCloseHospedagemInfo"></hospedagem-info>

    <!--<v-container class="p0">-->
        <v-layout row>
          <v-flex xs12 sm12>
            <!--<p>{{txt}} - {{styleGrid}}</p>-->
            <v-btn small color="cyan darken-4" class="white--text" @click="getDadosSemanaAnterior(dados.dataIni)"><v-icon dark left>chevron_left</v-icon>Semana Anterior</v-btn> <!-- fa-chevron-left -->
            <v-btn small color="cyan darken-4" class="white--text" @click="getDadosHoje()">Hoje</v-btn> <!-- fa-chevron-left -->
            <v-btn small color="cyan darken-4" class="white--text" @click="getDadosProximaSemana(dados.dataFim)">Semana Seguinte<v-icon dark right>chevron_right</v-icon></v-btn>
          </v-flex>
        </v-layout>

        <v-layout row wrap>
          <v-flex xs1 sm12 md3>
            <v-layout row wrap>
              <v-flex xs12 sm12>
                <v-date-picker v-model="dataAtual" 
                color="cyan darken-4" 
                locale="pt-br"
                class="mt-2 mr-2 ml-2"
                full-width></v-date-picker>
              </v-flex>
            </v-layout>
          </v-flex>


          <v-flex xs10 sm12 md9>

            <v-tabs v-model="tabActive" slider-color="cyan darken-2">
              <v-tab>Mapa de Hospedagem</v-tab>
              <v-tab>Hóspedes na Semana</v-tab>
              <v-tab>Resumo Diário</v-tab>

              <!-- início mapa -->
              <v-tab-item>
                <div class="mb-10">&nbsp;</div>

                <v-layout row>
                  <v-flex sm12>
                    <div class="box p4 laranja h60">
                    <center>Quarto/<br/>Leito</center>
                    </div>
                  </v-flex>
                  <v-flex sm12 v-for="(dia, index) in dados.dias" :key="index" @click="selecionarDia(dia, index)"
                  :class="{'white--text': isDataAtual(dia), 'cyan darken-4':isDataAtual(dia), 'amber lighten-4':!isDataAtual(dia) }">
                    <div class="box p4 h60 " style="cursor:pointer;">
                      <span style="font-size: 10pt">{{diaSemana(dia)}}</span><br/>
                      <span style="font-size: 14pt">{{formatDate(dia,'DD/MMM')}}</span>
                    </div>
                  </v-flex>
                    <div class="w15"></div>
                </v-layout>
                
                <div :style="styleGrid" class="scroll-y p0">

                  <v-layout row v-for="(celula, index) in dados.leitos" :key="index">
                    <v-flex sm12>
                      <div class="box p4 laranja hleito" :style="{height:calcularAlturaLeito(index)}">
                        <center v-if="celula.quartoNumero != '9999'">{{celula.quartoNumero}}-{{celula.leitoNumero}}</center>
                        <center v-if="celula.quartoNumero == '9999'">Parcial</center>
                      </div>
                    </v-flex>

                    <v-flex sm12 v-for="(cell, indice) in dados.dias" :key="indice" style="background:#f5f5f5;" :class="{'grey lighten-2':isIndiceDataAtual(indice)}">
                      <div class="box hleito" v-if="celula.hospedagens.length == 0"></div>
                      <div class="box hleito" v-for="(hospedagem, hIdx) in celula.hospedagens" :key="hIdx">
                        <div :class="hospedagemClass(hospedagem.dias[indice].identificador, hospedagem.dias[indice].classe)" 
                              v-if="hospedagem.dias[indice].identificador != '0'"
                              :style="{backgroundColor: colorStatus(hospedagem.dias[indice].identificador)}"
                              @click="showHospedagemInfoByIdentificador(hospedagem.dias[indice].identificador)">
                          <v-chip color="grey lighten-1" 
                            :small="true" text-color="black" 
                            v-if="hospedagem.dias[indice].firstIndex" 
                            class="chip">{{getNome(hospedagem.dias[indice].identificador)}}</v-chip>
                        </div>                              
                      </div>
                    </v-flex>


                    <!--
                    <v-flex sm12 v-for="(cell,indice) in celula.cells" :key="indice" style="background:#f5f5f5;" :class="{'grey lighten-2':isIndiceDataAtual(indice)}">
                      <div class="box hleito">
                        <div :class="hospedagemClass(cell.andamento)" v-if="cell.hospedagemId != 0" :style="{backgroundColor: colorStatus(cell.status)}" 
                        @click="showHospedagemInfo(cell.hospedagemId)">
                          <v-chip color="grey lighten-1" :small="true" text-color="black" v-if="cell.firstIndex" class="chip">{{getNome(cell.id)}}</v-chip>
                        </div>                              
                      </div>
                    </v-flex>
                    -->

                  </v-layout>
                </div>
              </v-tab-item>
              <!-- fim mapa -->

              <!-- início grid -->
              <v-tab-item>
                <v-flex xs12 sm12 md12>
                  <v-card flat>
                    <v-data-table :headers="headers" :items="pessoas" :dark="false" :persistent="true"
                    :rows-per-page-items="rowsperpage" :rows-per-page-text="'Linhas por página'"
                    class="elevation-1 mt-2 mr-2 ml-2">
                      <template slot="items" slot-scope="props">
                        <td class="text-xs-right">{{ props.item.hospedagemId }}</td>
                        <td class="text-xs-left font-weight-medium" :class="{'green--text': props.item.statusHospedagem=='ENCERRADA', 'indigo--text':props.item.statusHospedagem=='ABERTA', 'red--text':props.item.statusHospedagem=='VENCIDA'}">
                          {{ props.item.pessoaNome }}</td>
                        <td class="text-xs-left">{{ formatDate(props.item.dataEntrada,'DD/MMM') }}</td>
                        <td class="text-xs-left">{{ formatDate(props.item.dataPrevistaSaida,'DD/MMM') }}</td>
                        <td class="text-xs-left">{{ formatDate(props.item.dataEfetivaSaida,'DD/MMM') }}</td>
                        <td class="text-xs-left">{{ props.item.utilizacao }}</td>
                        <td class="text-xs-left">{{ props.item.leitoId != 0 ? props.item.quartoNumero + "-" + props.item.leitoNumero : ""}}</td>
                        <td class="text-xs-left">{{props.item.destinacaoHospedagemDescricao}}</td>
                        <td class="text-xs-left">
                          <v-icon class="mr-2 teal--text" @click="showHospedagemInfo(props.item.hospedagemId)">info</v-icon>
                        </td>
                      </template>
                    </v-data-table>    
                  </v-card>
                </v-flex>
              </v-tab-item>
              <!-- fim grid -->

              <!-- início dashboard -->
              <v-tab-item>
                <v-container grid-list-md text-xs-center>
                  <v-layout row wrap>

                    <v-flex xs4>
                      <v-card color="blue darken-2" class="white--text">
                        <v-card-title primary-title>
                          <div class="text-xs-center">
                            <div class="headline text-xs-center">
                              <v-icon dark left size="60">fa-users</v-icon>
                              <v-spacer></v-spacer>
                              <span class="font-weight-medium headline text-xs-center">{{qtdTotais}} Hóspedes</span>
                            </div>
                          </div>
                        </v-card-title>
                        <v-divider></v-divider>
                        <v-card-actions class="pa-3 grey lighten-4 black--text lighten-4">
                          <v-layout row wrap>
                            <v-flex xs4 class="text-xs-center">
                              <div class="font-weight-medium headline red--text">{{qtdVencidos}}</div>
                              <div class="caption text-uppercase grey--text">Vencidos</div>
                            </v-flex>
                            <v-flex xs4 class="text-xs-center">
                              <div class="font-weight-medium headline indigo--text">{{qtdPendentes}}</div>
                              <div class="caption text-uppercase grey--text">Pendentes</div>
                            </v-flex>
                            <v-flex xs4 class="text-xs-center">
                              <div class="font-weight-medium headline green--text">{{qtdEncerrados}}</div>
                              <div class="caption text-uppercase grey--text">Encerrados</div>
                            </v-flex>
                          </v-layout>  
                        </v-card-actions>
                      </v-card>
                    </v-flex>

                    <v-flex xs4>
                      <v-card color="red darken-2" class="white--text">
                        <v-card-title primary-title>
                          <div class="text-xs-center">
                            <div class="headline text-xs-center">
                              <v-icon dark left size="60">fa-bed</v-icon>
                              <v-spacer></v-spacer>
                              <span class="font-weight-medium headline text-xs-center">{{qtdLeitosTotais}} Leitos</span>
                            </div>
                          </div>
                        </v-card-title>
                        <v-divider></v-divider>
                        <v-card-actions class="pa-3 grey lighten-4 black--text lighten-4">
                          <v-layout row wrap>
                            <v-flex xs6 class="text-xs-center">
                              <div class="font-weight-medium headline red--text">{{qtdLeitosOcupados}}</div>
                              <div class="caption text-uppercase grey--text">Ocupados</div>
                            </v-flex>
                            <v-flex xs6 class="text-xs-center">
                              <div class="font-weight-medium headline green--text">{{qtdLeitosLivres}}</div>
                              <div class="caption text-uppercase grey--text">Livres</div>
                            </v-flex>
                          </v-layout>  
                        </v-card-actions>
                      </v-card>
                    </v-flex>

                    <v-flex xs4>
                      <v-card color="orange darken-2" class="white--text">
                        <v-card-title primary-title>
                          <div class="text-xs-center">
                            <div class="headline text-xs-center">
                              <v-icon dark left size="60">fa-utensils</v-icon>
                              <v-spacer></v-spacer>
                              <span class="font-weight-medium headline text-xs-center">{{qtdParciaisTotais}} Parciais</span>
                            </div>
                          </div>
                        </v-card-title>
                        <v-divider></v-divider>
                        <v-card-actions class="pa-3 grey lighten-4 black--text lighten-4">
                          <v-layout row wrap>
                            <v-flex xs4 class="text-xs-center">
                              <div class="font-weight-medium headline red--text">{{qtdParciaisVencidos}}</div>
                              <div class="caption text-uppercase grey--text">Vencidos</div>
                            </v-flex>
                            <v-flex xs4 class="text-xs-center">
                              <div class="font-weight-medium headline indigo--text">{{qtdParciaisPendentes}}</div>
                              <div class="caption text-uppercase grey--text">Pendentes</div>
                            </v-flex>
                            <v-flex xs4 class="text-xs-center">
                              <div class="font-weight-medium headline green--text">{{qtdParciaisEncerrados}}</div>
                              <div class="caption text-uppercase grey--text">Encerrados</div>
                            </v-flex>
                          </v-layout>  
                        </v-card-actions>
                      </v-card>
                    </v-flex>                 
                  </v-layout>
                </v-container>
              </v-tab-item>    
              <!-- fim dashboard -->
            </v-tabs>        
          </v-flex>
        </v-layout>
    <!--</v-container>      -->

  </div>
</template>
<script>

import Titulo from "../../components/Titulo.vue"
import HospedagemInfo from "./HospedagemInfo.vue"  

export default {
  name: 'Hospedagens',
  components:{
    Titulo,
    HospedagemInfo,
  },
  data: () =>({
    windowHeight: 0,
    styleGrid : 'max-height: 337px',
    txt : '???',
    dataAtual: null,
    dados: [],
    pessoas:[],

		qtdLeitosTotais : 0,
		qtdLeitosOcupados : 0,
    qtdLeitosLivres : 0,

		qtdTotais : 0,
		qtdVencidos : 0,
		qtdPendentes : 0,
		qtdEncerrados : 0,

		qtdParciaisTotais : 0,
		qtdParciaisVencidos : 0,
		qtdParciaisPendentes : 0,
		qtdParciaisEncerrados : 0,

    colorCache: {},
    headers: [
      { text: 'Hospedagem', align: 'left', value:'id', width: '5%',  sortable: false,},
      { text: 'Nome',  align:'left', value:'nome', width: '30%' },
      { text: 'Dt.Entrada',  align:'left', value:'dataEntrada', width: '5%' },
      { text: 'Dt.Prevista',  align:'left', value:'dataPrevistaSaida', width: '5%' },
      { text: 'Dt.Saída',  align:'left', value:'dataEfetivaSaida', width: '5%' },
      { text: 'Utilização',  align:'left', value:'tipoUtilizacao', width: '5%' },
      { text: 'Leito',  align:'left', value:'leitoId', width: '5%' },
      { text: 'Destinação',  align:'left', value:'destinacaoHospedagem', width: '2%' },
      { text: 'Ações',  align:'left', value:'nome', width: '2%',  sortable: false, }
    ],
    rowsperpage: [10,20,30,{"text":"Todos","value":-1}],
    tabActive : null,
  }),
  
  mounted () {
    //console.logconsole.log(this.$vuetify.breakpoint)
    this.$store.dispatch('setAcao','Hospedagens')

    this.windowHeight = window.innerHeight

    this.$nextTick(() => {
      window.addEventListener('resize', () => {
        this.windowHeight = window.innerHeight
      })
    })
    
  },

  watch: {
    dataAtual(){
      this.getDadosSemanaAtual()
      this.showEstatisticas()
    },

    estatisticaLeitos(){
      this.showEstatisticas()
    },

    windowHeight(newHeight, oldHeight) {
      this.txt = `mudou de ${oldHeight} para ${newHeight}`
      this.styleGrid = `max-height: ${newHeight - 272}px`
    }    
  },

  created(){
    this.dataAtual = petraDateTime.hoje()
  },

  computed: {

    estatisticaLeitos(){
      return this.dados.qtdLeitos
    },

    imageHeight () {
      switch (this.$vuetify.breakpoint.name) {
        case 'xs': return '220px - xs'
        case 'sm': return '400px - sm'
        case 'md': return '500px - md'
        case 'lg': return '600px - lg'
        case 'xl': return '800px - xl'
      }
    },

    alturaGrid () {
      switch (this.$vuetify.breakpoint.name) {
        case 'xs': return 'max-height: 337px'
        case 'sm': return 'max-height: 337px'
        case 'md': return 'max-height: 337px'
        
        case 'lg': return 'max-height: 337px'
        case 'xl': return 'max-height: 673px' //613
      }
    }    

  },

  methods: {

    selecionarDia(dia, index){
      this.dataAtual = dia;
    },

    //TODO Somente se dataAtual não estiver na lista de datas da semana. A não ser que force
    getDadosSemanaAtual(){
      var existe = this.contemElemento(this.dataAtual, this.dados.dias)
      if (!existe){
        this.getData(this.dataAtual)
      }
    },

    refreshMapa(){
      this.getData(this.dataAtual)
    },

    contemElemento(obj, list) {
      for (var x in list) {
          if (list[x] === obj) {
              return true;
          }
      }
      return false;
    },

    getDadosHoje(){
      this.dataAtual = petraDateTime.hoje()
    },

    getDadosSemanaAnterior(data){
      this.dataAtual = petraDateTime.semanaAnterior(data)
    },

    getDadosProximaSemana(data){
      this.dataAtual = petraDateTime.semanaSeguinte(data)
    },

    isDataAtual(data){
      return data == this.dataAtual ? true : false
    },

    isIndiceDataAtual(indice){
      return indice == petraDateTime.getIndiceData(this.dataAtual)
    },

    getData(data) {
      var dados = {
        data : data
      }
      petra.axiosPost("/app/hospedagem/mapa", dados)
        .then(response => {
            this.dados = response.data
            console.log("getData() ",this.dados)
            this.pessoas = response.data.hospedagens
            //console.log(this.pessoas)
            this.showEstatisticas()
        })
        .catch(error => {

        })
    },

    showEstatisticas(){
      var index = petraDateTime.getIndiceData(this.dataAtual) || 0

      if (this.dados && this.dados.qtdLeitosTotais){
        this.qtdLeitosTotais = this.dados.qtdLeitosTotais[index]
        this.qtdLeitosOcupados = this.dados.qtdLeitosOcupados[index]
        this.qtdLeitosLivres = this.dados.qtdLeitosLivres[index]

        this.qtdTotais = this.dados.qtdTotais[index]
        this.qtdVencidos = this.dados.qtdVencidos[index]
        this.qtdPendentes = this.dados.qtdPendentes[index]
        this.qtdEncerrados = this.dados.qtdEncerrados[index]

        this.qtdParciaisTotais = this.dados.qtdParciaisTotais[index]
        this.qtdParciaisVencidos = this.dados.qtdParciaisVencidos[index]
        this.qtdParciaisPendentes = this.dados.qtdParciaisPendentes[index]
        this.qtdParciaisEncerrados = this.dados.qtdParciaisEncerrados[index]
      } else {
        this.qtdLeitosTotais = 0
        this.qtdLeitosOcupados = 0
        this.qtdLeitosLivres = 0

        this.qtdTotais = 0
        this.qtdVencidos = 0
        this.qtdPendentes = 0
        this.qtdEncerrados = 0

        this.qtdParciaisTotais = 0
        this.qtdParciaisVencidos = 0
        this.qtdParciaisPendentes = 0
        this.qtdParciaisEncerrados = 0
      }
    },

    recarregar(){
      this.getDadosSemanaAtual()
    },

    onEncerrada(hospedagemId){
      this.refreshMapa()
    },

    onCloseHospedagemInfo(){
      this.refreshMapa();
    },

    formatDate(data,formato){
      return petraDateTime.formatDate(data,formato)
    },

    diaSemana(dia){
      return petraDateTime.diaSemana(dia)
    },

    hospedagemClass(id, classe){
      var hospedagem = this.getHospedagemById(id);
      if (hospedagem){
          if (classe == "INICIO") {
            return 'grafico grafico_inicio'
          } else 
          if (classe == "DURANTE") {
            return 'grafico grafico_durante'
          } else 
          if (classe == "FIM") {
            return 'grafico grafico_fim'
          } else
          if (classe == "INDO") {
            return 'grafico grafico_indo'
          } else      
          if (classe == "VINDO") {
            return 'grafico grafico_vindo'
          } else
          if (classe == "INDO_VINDO") {
            return 'grafico grafico_indo grafico_vindo'
          } else
          if (classe == "VINDO_FIM") {
            return 'grafico grafico_vindo grafico_fim'
          }
      }
      return ''
    },  

    colorStatus(id){
      var hospedagem = this.getHospedagemById(id);
      if (hospedagem){
        if (hospedagem.baixado) {
          return 'orange'
        } else if (hospedagem.statusHospedagem == 'ABERTA'){
          return '#0D47A1' // blue darken-4
        } else if (hospedagem.statusHospedagem == 'ENCERRADA'){
          return '#2E7D32' // green darken-4
        } else if (hospedagem.statusHospedagem == 'VENCIDA'){
          return '#D50000' // red accent-4
        }
      }
      return 'blue'
    },

    calcularAlturaLeito(index){
      var qtd = 0

      if (this.dados && this.dados.leitos){
        qtd = this.dados.leitos[index].hospedagens.length
      }

      if (qtd <= 1) {
        return '28px'
      } else {
        return  (qtd*28)+'px'
      }
    },

    getHospedagemById(id){
      return this._.find(this.dados.hospedagens,{identificador : id});
    },

    getNome(id){
      var hospedagem = this.getHospedagemById(id);
      if (hospedagem){
        var nome = hospedagem.pessoaNome.split(" ")[0];
        if (nome.length > 4){
          nome = nome.substr(0,4) + "..."
        }
        return nome;
      }
      return '???';
    },

    showHospedagemInfoByIdentificador(id){
      var hospedagem = this.getHospedagemById(id);
      if (hospedagem){
        this.showHospedagemInfo(hospedagem.hospedagemId)
      }
    },

    showHospedagemInfo(id){
      this.$refs.hospedagemInfo.openDialog(id);
    },

    exemplosMoment(){
      /* 

      https://tableless.com.br/trabalhando-com-moment/

      const dia = moment("2018-25-02")
      moment("abcxyz").isValid() // false
      moment("2018-02-24").add(2, "days") // 2018-02-26
      moment("2018-02-24").add(1, "year").subtract("1", "days") // 2019-02-23

      moment().format("dd/MM/yyyy HH-mm") // 25/02/2018 13-35
      moment("abcxyz").format('YYYY MM DD') // "Invalid date"

      moment('2017-10-20').isBefore('2017-10-21'); // true
      moment('2017-10-20').isBefore('2010-12-31', 'year'); // false
      moment('2017-10-20').isBefore('2018-01-01', 'year'); // true

      moment('2010-10-20').isBetween('2010-10-19', '2010-10-25'); // true
      */      
    }

  }
}
//click="testarOperacoes(dia.hospedagem.hospedagemId, dados.dias[indice])" 

</script> 
<style scoped>

:root {
  --margin: 2px;
}

.w15{
  min-width:15px;
  color:red;
}

.fonte-azul{
  color: blue;
}

.grade {
  /*padding: 10px;*/
  width: 100%;
}

.container-box {
  display: flex;
  flex-wrap: wrap;
}

.box {
  box-sizing: border-box;
  border-radius: 0px;
  margin: 0px;
  height: 30px;
  border: 1px #bdbdbd solid;
}

.h60 {
  height: 54px;
}

.hleito{
  /*height:48px;*/
  height:28px; /* era 36px */
}

.chip {
  cursor:pointer;
  margin:0px;
  padding:1px;
  font-size:8pt;
  text-transform:uppercase;
  margin-left:15px;
}

.p0{
  padding:0px;
}

.p1{
  padding: 1px;
}

.p4 {
  padding:4px;
}

.laranja {
  background-color: #FFECB3;
}

.grafico{
  background-color: #E57373;
  margin-left:-1px;
  margin-right:-1px;
  margin-top: 1px;
  height: calc(100% - 2px);
  cursor:pointer;
  padding: 0px;
}

.grafico_inicio {
  border-top-left-radius: 20px;
  border-bottom-left-radius: 20px;
  width: calc(100% - 2px);
  margin-left: 3px;
}

.grafico_fim {
  border-top-right-radius: 20px;
  border-bottom-right-radius: 20px;
  width: calc(100% - 3px);
}

/*  https://css-tricks.com/the-shapes-of-css/ */
.grafico_indo {
  border-top-right-radius: 30px;
  border-bottom-right-radius: 0px;
  /*width: calc(100% - 3px);*/
}

.grafico_vindo {
  border-top-left-radius: 0px;
  border-bottom-left-radius: 30px;
  /*width: calc(100% - 2px);*/
  /*margin-left: 3px;*/
}

</style>