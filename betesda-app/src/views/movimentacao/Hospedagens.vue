<template>
  <div>
    <!--<hospedagem-info ref="hospedagemInfo" @save="recarregar" @encerrada="onEncerrada" @close="onCloseHospedagemInfo"></hospedagem-info>-->

    <el-container v-if="state == 'browse'">
      <el-header>
        <el-button type="primary" @click="getDadosSemanaAnterior(dados.dataIni)">Semana Anterior</el-button>
        <el-button type="primary" @click="getDadosHoje()">Hoje</el-button>
        <el-button type="primary" @click="getDadosProximaSemana(dados.dataFim)">Semana Seguinte</el-button>
      </el-header>
      <el-main>
        <el-row type="flex" :gutter="10">
          <el-col :sm="8" :md="8" :lg="8" style="height:400px;">
            <calendar-view
              :show-date="showDate"
              :time-format-options="{hour: 'numeric', minute:'2-digit'}"
              :enable-drag-drop="true"
              :disable-past="disablePast"
              :disable-future="disableFuture"
              :show-event-times="showEventTimes"
              :display-period-uom="displayPeriodUom"
              :display-period-count="displayPeriodCount"
              :starting-day-of-week="startingDayOfWeek"
              :class="themeClasses"
              :period-changed-callback="periodChanged"
              :current-period-label="useTodayIcons ? 'icons' : ''"
              @drop-on-date="onDrop"
              @click-date="onClickDay"
              @click-event="onClickEvent"
            >
              <calendar-view-header
                slot="header"
                slot-scope="{ headerProps }"
                :header-props="headerProps"
                @input="setShowDate"
              />
            </calendar-view>

          </el-col>

          <el-col :sm="16" :md="16" :lg="16">

            <el-tabs type="border-card" v-model="activeTabName" @tab-click="handleTabClick">
              <el-tab-pane label="Mapa de Hospedagem" name="mapa">

                <el-container style="margin:2px;">
                  <el-container :style="styleContainerMapa">
                    <el-header style="line-height:20px; height:54px;">
                      <el-row type="flex">
                        <el-col>
                          <div class="thebox p4 laranja h60">
                            <span style="font-size: 12pt; align:center;">Quarto/</span>
                            <span style="font-size: 12pt; align:center;">Leito</span>
                          </div>
                        </el-col>

                        <el-col v-for="(dia, index) in dados.dias" :key="index" 
                          :class="{'white-text': isDataAtual(dia), 'teal-darken-2':isDataAtual(dia), 'amber-lighten-4':!isDataAtual(dia) }">
                          <div class="thebox p4 h60 " style="cursor:pointer; line-height:8px" @click="selecionarDia(dia, index)">
                            <p style="font-size: 9pt">{{diaSemana(dia)}}</p>
                            <p style="font-size: 12pt">{{formatDate(dia,'DD/MMM')}}</p>
                          </div>
                        </el-col>
                        <div class="w15"></div>
                      </el-row>                      
                    </el-header>
                    
                    <el-main style="padding-top:0px;">

                      <div :style="styleGrid" class="scroll-y p0">
                        <el-row type="flex" v-for="(celula, index) in dados.leitos" :key="index">
                          <el-col>
                            <div class="thebox p4 laranja hleito" :style="{height:calcularAlturaLeito(index)}">
                              <center v-if="celula.quartoNumero != '9999'">{{celula.quartoNumero}}-{{celula.leitoNumero}}</center>
                              <center v-if="celula.quartoNumero == '9999'">Parcial</center>
                            </div>
                          </el-col>

                          <el-col sm12 v-for="(cell, indice) in dados.dias" :key="indice" style="background:#f5f5f5;" :class="{'grey-lighten-2':isIndiceDataAtual(indice)}">
                            <div class="thebox hleito" v-if="celula.hospedagens.length == 0"></div>
                            <div class="thebox hleito" v-for="(hospedagem, hIdx) in celula.hospedagens" :key="hIdx">
                              <div :class="hospedagemClass(hospedagem.dias[indice].identificador, hospedagem.dias[indice].classe, hospedagem.dias[indice])" 
                                    v-if="hospedagem.dias[indice].identificador != '0'"
                                    :style="{backgroundColor: colorStatus(hospedagem.dias[indice].identificador)}"
                                    @click="showHospedagemInfoByIdentificador(hospedagem.dias[indice].identificador)">
                                    <!--<el-tag type="info" size="mini">Mini</el-tag>-->
                                    
                                <div class="chip" v-if="hospedagem.dias[indice].firstIndex">{{getNome(hospedagem.dias[indice].identificador)}}</div>
                              </div>                              
                            </div>
                          </el-col>
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
                        </el-row>
                      </div>

                    </el-main>
                  </el-container>
                </el-container>

              </el-tab-pane>
              <el-tab-pane label="Hóspedes na Semana" name="hospedes">
                
              </el-tab-pane>
              <el-tab-pane label="Resumo Diário" namd="resumo">
                
              </el-tab-pane>
            </el-tabs>

          </el-col>
        </el-row>
      </el-main>
    </el-container>

    <tela-hospedagem v-if="state=='info'" @close="onCloseInfo" :id="hospedagemSelecionada"></tela-hospedagem>
  </div>

</template>

<script>
  import HospedagemInfo from "./HospedagemInfo.vue"  
  import TelaHospedagem from "../hpd/TelaHospedagem.vue"


import {
  CalendarView,
	CalendarViewHeader,
  CalendarMathMixin,
} from "vue-simple-calendar"


require("vue-simple-calendar/static/css/default.css")

export default {
  name: 'Hospedagens',

  components: {
		CalendarView,
    CalendarViewHeader,
    HospedagemInfo,
    TelaHospedagem
	},
	mixins: [CalendarMathMixin],  

  created(){
  },

  mounted(){
    this.$store.dispatch('setAcao','Hospedagens')

    this.windowHeight = window.innerHeight

    this.$nextTick(() => {
      window.addEventListener('resize', () => {
        this.windowHeight = window.innerHeight
      })
    })

  },

  data: () =>({
    dados: [],

    erros: [],

    activeTabName: 'mapa',

    showTelaHospedagem : false,
    state : "browse",
    hospedagemSelecionada : null,


    windowHeight: 0,
    styleGrid : 'max-height: 337px',
    styleContainerMapa : 'height:300px',
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

    toolTipDelay: 500,

			showDate: null, /*this.thisMonth(1),*/
			message: "",
			startingDayOfWeek: 0,
			disablePast: false,
			disableFuture: false,
			displayPeriodUom: "month",
			displayPeriodCount: 1,
			showEventTimes: true,
			newEventTitle: "",
			newEventStartDate: "",
			newEventEndDate: "",
			useDefaultTheme: true,
			useHolidayTheme: true,
			useTodayIcons: false,    

  }),

  watch: {

    /*
    hospedagemSelecionada(newVal, oldVal){
      console.log("mudou hospedagemSelecionada para "+newVal)
    },  
    */

    dataAtual(){

      var d = moment(this.dataAtual).toDate()

      //new Date(t.getFullYear(), t.getMonth(), d, h || 0, m || 0)


      this.setShowDate(d)
      this.getDadosSemanaAtual()
      this.showEstatisticas()
    },

    estatisticaLeitos(){
      this.showEstatisticas()
    },

    windowHeight(newHeight, oldHeight) {
      this.txt = `mudou de ${oldHeight} para ${newHeight}`
      this.styleGrid = `max-height: ${newHeight - 272}px`
      this.styleContainerMapa = `height: ${newHeight-270}px`
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
    },
    
		userLocale() {
			return this.getDefaultBrowserLocale
		},
		dayNames() {
			return this.getFormattedWeekdayNames(this.userLocale, "long", 0)
		},
		themeClasses() {
			return {
				"theme-default": this.useDefaultTheme,
				"holiday-us-traditional": this.useHolidayTheme,
				"holiday-us-official": this.useHolidayTheme,
			}
		},
    

  },

  methods: {

    handleTabClick(tab, event) {
      //console.log(tab, event);
    },

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
            console.log(this.dados)
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

    hospedagemClass(id, classe, dias){
      var hospedagem = this.getHospedagemById(id);

      hospedagem.baixado

      //console.log('hpd:',hospedagem)
      //console.log('dias:',dias)
      if (hospedagem){
          if (classe == "BAIXADO"){
            return 'grafico grafico_fim_baixado'
          } else
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
        if (hospedagem.statusHospedagem == 'ABERTA'){
          return '#00796B' // teal darken-2
        } else if (hospedagem.statusHospedagem == 'ENCERRADA'){
          return '#78909C' // blue-grey lighten-1
        } else if (hospedagem.statusHospedagem == 'VENCIDA'){
          return '#D50000' // red accent-4
        } else if (hospedagem.baixado) {
          return '#4DB6AC'
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
      return _.find(this.dados.hospedagens,{identificador : id});
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
      //console.log("this.$refs.hospedagemInfo ",this.$refs.hospedagemInfo)

      this.state = "info"
      this.hospedagemSelecionada = id;
      this.showTelaHospedagem = true;

      //this.$refs.hospedagemInfo.openDialog(id);
    },

    onCloseInfo(){
      this.state = "browse"
      this.hospedagemSelecionada = null;
      this.$store.dispatch('setAcao','Hospedagens')
      this.recarregar()
    },

		periodChanged(range, eventSource) {
			// Demo does nothing with this information, just including the method to demonstrate how
			// you can listen for changes to the displayed range and react to them (by loading events, etc.)
      //console.log(eventSource) //watch
      
      //var d = range.periodStart
      //this.dataAtual = moment(d).format("YYYY-MM-DD")
			//console.log(range.periodStart)
    },
    
		thisMonth(d, h, m) {
			const t = new Date()
			return new Date(t.getFullYear(), t.getMonth(), d, h || 0, m || 0)
    },
    
		onClickDay(d) {

      this.dataAtual = moment(d).format("YYYY-MM-DD")

      //console.log(`You clicked: ${this.dataAtual}`)

    },
    
		onClickEvent(e) {
			this.message = `You clicked: ${e.title}`
    },
    
		setShowDate(d) {
			this.message = `Changing calendar view to ${d.toLocaleDateString()}`
      this.showDate = d
      this.dataAtual = moment(d).format("YYYY-MM-DD")
    },
    
		onDrop(event, date) {
			this.message = `You dropped ${event.id} on ${date.toLocaleDateString()}`
			// Determine the delta between the old start date and the date chosen,
			// and apply that delta to both the start and end date to move the event.
			const eLength = this.dayDiff(event.startDate, date)
			event.originalEvent.startDate = this.addDays(event.startDate, eLength)
			event.originalEvent.endDate = this.addDays(event.endDate, eLength)
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
</script>

<style scoped>
  .el-header {
    line-height: 60px;
  }
  
  .container{
    width:800px;
  }

  .el-col{
    padding-bottom:0px;
  }

  .el-form{
    padding:10px;
  }

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

.thebox {
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
  height:28px;
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
  border-color:#000;
  border-style: dotted;
  border-width: 0px 4px 0px 0px;
}

.grafico_fim_baixado {
  border-color:#000;
  border-style: solid;
  border-width: 0px 4px 0px 0px;
}

.grafico_vindo {
  border-color:#000;
  border-style: dotted;
  border-width: 0px 0px 0px 4px;
}

.teal-darken-2 {
  background-color: #00796B;
}

.white-text{
    color:white;
}
.amber-lighten-4{
    background-color:#FFECB3;
}
/*
.chip {
  cursor:pointer;
  margin:0px;
  padding:1px;
}
*/
.chip{
  font-size:8pt;
  margin-left:4px;
  background-color:white;
  width:48px;
  text-align:center;
  text-transform:uppercase;
  line-height:1em;
  margin-top:1px;
  height: 10px;
  padding-top:8px;
  padding-bottom: 4px;
  border: 1px solid #bbb;
  border-radius: 16px;
}


</style>