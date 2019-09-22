<template>
  <div>
    <el-container v-if="state == 'browse'">
      <el-header>
        <el-row type="flex" style="line-height:1em;">
          <el-button type="primary" @click="getDadosSemanaAnterior(dados.dataIni)">Semana Anterior</el-button>
          <el-button type="primary" @click="getDadosHoje()">Hoje</el-button>
          <el-button type="primary" @click="getDadosProximaSemana(dados.dataFim)">Semana Seguinte</el-button>

          <div class="block-date">
            <span style="width:200px; text-align:right; margin-right: 10px; padding-top:10px;">Data Selecionada:</span>
            <el-date-picker v-model="dataAtual" type="date" format="dd/MM/yyyy" value-format="yyyy-MM-dd" style="width: 100%;" ></el-date-picker>
          </div>
        </el-row>

      </el-header>
      <el-main>
        <el-row type="flex" :gutter="10">
          <el-col :sm="24" :md="24" :lg="24">

            <el-tabs type="border-card" v-model="activeTabName" @tab-click="handleTabClick">
              <el-tab-pane label="New Mapa de Hospedagem" name="mapa">

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
                        <div class="container-mapa" v-for="(celula, index) in linhas" :key="index">
                          <div class="titulo">
                              <center v-if="celula.quartoNumero != '0'">{{celula.quartoNumero}}-{{celula.leitoNumero}}</center>
                              <center v-if="celula.quartoNumero == '0'">Parcial</center>
                          </div> 
                          <div>
                            <div class="container-linha" v-for="(h, idx) in celula.hospedagens" :key="idx">
                              <div v-for="(classe, idxClasses) in h.classes" :key="idxClasses" class="celula-borda-cinza">

                                <el-popover
                                    v-if="h.identificador != null && classe != ''"
                                    placement="top-start"
                                    width="300"
                                    :title="'HPD: '+ h.identificador"
                                    trigger="hover"
                                    :open-delay="1000"
                                    :content="h.nome">
                                    <div slot="reference" :class="newHospedagemClass(classe, h.statusHospedagem)" 
                                          @click="showHospedagemInfo(h.hospedagemId)">
                                          <p v-if="idxClasses == h.idxIni" class="texto-linha">{{getPrimeiroNome(h.nome)}}</p>
                                    </div>
                                </el-popover>

                              </div>
                            </div>
                          </div>
                        </div>  
                      </div>

                    </el-main>
                  </el-container>
                </el-container>
              </el-tab-pane>

              <!-- :row-class-name="tableRowClassName"  -->
              <el-tab-pane label="Hóspedes na Semana" name="hospedes">
                <el-row type="flex" justify="center" align="middle">
                  <el-col :sm="24" :md="24" :lg="24">

                  <table>
                      <thead>
                        <tr>
                          <th>Nome</th>
                          <th>Cidade</th>
                          <th>Utilização</th>
                          <th>Status Hosped.</th>
                          <th>Entrada</th>
                          <th>Prev.Saída</th>
                          <th>Dt. Saída</th>
                          <th>Ações</th>
                        </tr>
                      </thead>
                      <tbody>
                          <tr v-for="(h, idxHpd) in hospedes" :key="idxHpd">
                            <td>{{h.pessoaNome}}</td>
                            <td>{{h.pessoaCidadeUfOrigem}}</td>
                            <td>{{h.tipoUtilizacaoDescricao}}</td>
                            <td>{{h.statusHospedagem}}</td>
                            <td>{{formatDate(h.dataPrimeiraEntrada,'DD/MM')}}</td>
                            <td>{{formatDate(h.dataPrevistaSaida,'DD/MM')}}</td>
                            <td>{{formatDate(h.dataEfetivaSaida,'DD/MM')}}</td>
                            <td>

                            <el-tooltip content="Ver Detalhes" placement="bottom" :open-delay="300">
                              <el-button type="primary" plain size="mini" circle @click="showHospedagemInfo(h.hospedagemId)">
                                <i class="fas fa-info"></i>
                              </el-button>
                            </el-tooltip>

                            <el-tooltip content="Editar Pessoa" placement="bottom" :open-delay="300">
                              <el-button type="primary" plain size="mini" circle @click="handleEditPessoa(h.pessoaId)">
                                <i class="fas fa-pencil-alt"></i>
                              </el-button>
                            </el-tooltip>


                            </td>
                          </tr>
                      </tbody>
                    </table>                    
                  </el-col>
                </el-row>
              </el-tab-pane>

              <!--
              <el-tab-pane label="Hóspedes por Cidade de Origem" name="cidades">
                  <el-container :style="styleContainerMapa">
                    <el-main style="padding-top:0px;">          
                        <el-collapse accordion>
                          <el-collapse-item :title="cidade.nome" v-for="(cidade, index) in cidades" :key="index">

                            <div class="flex-container wrap">
                              <el-card class="flex-item" v-for="(hospedagem, idx) in cidade.hospedagens" :key="idx" shadow="never" style="padding:5px; width:250px; line-height:1.5em;" 
                              :style="{backgroundColor: colorStatusItem(hospedagem.statusHospedagem)}"
                              >
                                <div style="font-weight:bold; font-size:1.1em;">{{hospedagem.pessoaNome}}

                                  <el-tooltip content="Editar" placement="bottom" :open-delay="toolTipDelay">
                                    <el-button type="primary" plain size="mini" circle @click="handleEditPessoa(hospedagem.pessoaId)">
                                      <i class="fas fa-pencil-alt"></i>
                                    </el-button>
                                  </el-tooltip>

                                </div>
                                <div>Utilização: <span style="font-weight:bold;">{{hospedagem.utilizacao}}</span>
                                  <span v-if="hospedagem.utilizacao == 'TOTAL'">Leito: <span style="font-weight:bold;">{{hospedagem.quartoNumero}}-{{hospedagem.leitoNumero}}</span></span> 
                                </div>                      
                                <div>Status: <span style="font-weight:bold;">{{hospedagem.statusHospedagem}}</span>
                                
                                  <el-tooltip content="Ver Detalhes" placement="bottom" :open-delay="300">
                                    <el-button type="primary" plain size="mini" circle @click="showHospedagemInfo(hospedagem.hospedagemId)">
                                      <i class="fas fa-info"></i>
                                    </el-button>
                                  </el-tooltip>
                          
                                </div>                      
                              </el-card>
                            </div>
                            
                          </el-collapse-item>
                        </el-collapse>  
                    </el-main>  
                </el-container>  
              </el-tab-pane>
              -->
              <el-tab-pane label="Quadro de Hospedagens" name="quadro">
                  <el-container :style="styleContainerMapa">

                    <el-header style="line-height:20px; height:54px;">
                      <el-row type="flex">
                        <el-col v-for="(dia, index) in dados.dias" :key="index" 
                          :class="{'white-text': isDataAtual(dia), 'teal-darken-2':isDataAtual(dia), 'amber-lighten-4':!isDataAtual(dia) }">
                          <div class="thebox p4 h60 " style="cursor:pointer; line-height:8px; text-align:center; font-size: 10pt;"  @click="selecionarDia(dia, index)">
                            <p>{{diaSemana(dia)}}</p>
                            <p>{{formatDate(dia,'DD/MMM')}}</p>
                          </div>
                        </el-col>
                      </el-row>                      
                    </el-header>

                    <el-main style="padding-top:10px;">

                      <el-col :sm="10" :md="10" :lg="10">

                        <el-row type="flex" v-for="(quarto, index) in quadro.quartos" :key="index">
                          <el-col v-for="(leito, lid) in quarto.leitos" :key="lid">
                            <div v-if="leito.id != 0" :class="quadroClass(0, index, lid)"  style="text-align:center; cursor:pointer;" @click="getHospedagensByLeitoId(leito.id)">
                              {{quarto.numero}}-{{leito.numero}}
                            </div>
                            <div class="thebox-circular cinza" v-if="leito.id == 0">
                              
                            </div>
                          </el-col>
                        </el-row>
                      </el-col>
                      <el-col :sm="14" :md="14" :lg="14">
                          <div class="subtitulo bg-purple">Quarto {{quadroQuartoNumero}} Leito {{quadroLeitoNumero}}</div>

                          <div class="flex-container wrap">
                            <!-- COMPONENTIZAR!!!! -->
                            <el-card class="flex-item" v-for="(hq, idx) in hospedagensQuadro" :key="idx" shadow="never" style="font-size:10pt; padding:5px; width:350px; line-height:1.5em;" 
                            :style="{backgroundColor: colorStatusItem(hq.statusHospedagem)}"
                            >
                              <div style="font-weight:bold; font-size:1.1em;">{{hq.pessoaNome}}

                                <el-tooltip content="Editar" placement="bottom" :open-delay="toolTipDelay">
                                  <el-button type="primary" plain size="mini" circle @click="handleEditPessoa(hq.pessoaId)">
                                    <i class="fas fa-pencil-alt"></i>
                                  </el-button>
                                </el-tooltip>

                              </div>
                              <div>Cidade de Origem: <span style="font-weight:bold;">{{hq.pessoaCidadeUfOrigem}}</span></div>
                              <div>Fone: <span style="font-weight:bold;">9999-9999</span></div>
                              <div>Utilização: <span style="font-weight:bold;">{{hq.tipoUtilizacaoDescricao}}</span>
                                <span v-if="hq.tipoUtilizacao == 'T'">Leito: <span style="font-weight:bold;">{{hq.hospedagem.quartoNumero}}-{{hq.hospedagem.leitoNumero}}</span></span> 
                              </div>                      
                              <div>Status: <span style="font-weight:bold;">{{hq.statusHospedagem}}</span>
                              
                                <el-tooltip content="Ver Detalhes" placement="bottom" :open-delay="300">
                                  <el-button type="primary" plain size="mini" circle @click="showHospedagemInfo(hq.hospedagemId)">
                                    <i class="fas fa-info"></i>
                                  </el-button>
                                </el-tooltip>
                        
                              </div>                      
                            </el-card>
                          </div>

                      </el-col>

                    </el-main>  
                </el-container>  
              </el-tab-pane>
            </el-tabs>

          </el-col>
        </el-row>
      </el-main>
    </el-container>

    <tela-hospedagem v-if="state=='info'" @close="onCloseInfo" :id="hospedagemSelecionada"></tela-hospedagem>

    <div>
      <el-dialog title="Detalhes da Pessoa" :visible.sync="editPessoa" width="800px">
        <el-row type="flex">
          <el-col>
            <frame-pessoa :id="pessoaId" :canSelecionar="false" @cancel="onCancelPessoa" @save="onSavePessoa"></frame-pessoa>      
          </el-col>
        </el-row>
      </el-dialog>
    </div>

  </div>

</template>

<script>
  import TelaHospedagem from "../hpd/TelaHospedagem.vue"
  import FramePessoa from "../cadastros/FramePessoa"


export default {
  name: 'NewHospedagens',

  components: {
    TelaHospedagem,
    FramePessoa
	},

  created(){
  },

  mounted(){
    this.$store.dispatch('setAcao','New Hospedagens')

    this.windowHeight = window.innerHeight

    this.$nextTick(() => {
      window.addEventListener('resize', () => {
        this.windowHeight = window.innerHeight
        this.tableHeight = window.innerHeight - 300
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
    
    tableHeight : window.innerHeight - 300,

    txt : '???',
    editPessoa : false,
    pessoaId : 0,
    dataAtual: null,
    
    //diaIndex representa o índice do dia clicado
    diaIndex: 0,
    quadroQuartoNumero : null,
    quadroLeitoNumero : null,

    form: {
      dataBase: null
    },
    dados: [],
    pessoas:[],
    cidades:[],
    quadro:[],
    hospedagensQuadro: [],
    hospedes:[],

    //VALENDO!!!!
    linhas:[],

    colorCache: {},

    toolTipDelay: 500,

    showDate: null, 
    message: "",
    startingDayOfWeek: 0,
    disablePast: false,
    disableFuture: false,
    displayPeriodUom: "month",
    displayPeriodCount: 1,
    showEventTimes: true,
    useDefaultTheme: true,
    useHolidayTheme: true,
    useTodayIcons: false,    

  }),

  watch: {

    dataAtual(){
      this.hospedagensQuadro = []
      this.quadroQuartoNumero = null
      this.quadroLeitoNumero = null

      var d = moment(this.dataAtual).toDate()
      this.diaIndex = petraDateTime.getIndiceData(this.dataAtual)

      this.setShowDate(d)
      this.getDadosSemanaAtual()
    },

    windowHeight(newHeight, oldHeight) {
      this.txt = `mudou de ${oldHeight} para ${newHeight}`
      this.styleGrid = `max-height: ${newHeight - 272}px`
      this.styleContainerMapa = `height: ${newHeight-270}px`
      this.tableHeight = `${newHeight-270}`
    }    
  },

  created(){
    this.dataAtual = petraDateTime.hoje()
  },

  computed: {

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
        case 'xl': return 'max-height: 673px'
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
    },

    handleEditPessoa(id) {
      this.pessoaId = id
      this.editPessoa = true;
    },

    onCancelPessoa(id) {
      this.pessoaId = null
      this.editPessoa = false
    },

    onSavePessoa(id) {
      this.pessoaId = null
      this.editPessoa = false
      this.refreshMapa()
    },


    selecionarDia(dia, index){
      this.dataAtual = dia
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
      petra.axiosPost("/app/hospedagem/mapa_new", dados)
        .then(response => {
            this.dados = response.data
            this.linhas = response.data.linhas;
            this.hospedes = response.data.pessoas;

            //console.log(this.dados)

            this.pessoas = response.data.hospedagens
            var cidades = []
            for (var c in this.dados.porCidade){
              var cidade = {
                nome : c, 
                hospedagens : []
              }
              for (var id in this.dados.porCidade[c]){
                const identificador = this.dados.porCidade[c][id]
                var hospedagem = this.getHospedagemById(identificador)
                cidade.hospedagens.push(hospedagem)
               }
              cidades.push(cidade)
            }
            this.cidades = cidades

            this.quadro = response.data.quadro
            
        })
        .catch(error => {

        })
    },

    tableRowClassName({row, rowIndex}) {

      if (row.statusHospedagem == "ABERTA"){
        return 'green-row'
      } else
      if (row.statusHospedagem == "VENCIDA"){
        return 'red-row'
      } 
      return ''
    },
    
    fmtDate(row, col, cellValue, index){
      return petraDateTime.formatDate(cellValue, "DD/MM");
    },


    fmtLeito(row, col){
      return (row.leitoId != 0 ? row.quartoNumero + "-" + row.leitoNumero : "")
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

    quadroClass(dia, quarto, leito){
      var conteudo = this.quadro.quartos[quarto].leitos[leito].dias[this.diaIndex];

      if (conteudo == 1){
        return "thebox-circular teal-darken-2 fonte-branca";  
      }
      return "thebox-circular";
    },

    // NOVO
    newHospedagemClass(classe, status){

      var classeStatus =  this.newColorStatusItem(status)
      var retorno = 'grafico ' + classeStatus + ' ';
      
      if (classe == "BAIXADO"){
        return retorno + 'grafico_fim_baixado'
      } else
      if (classe == "INICIO") {
        return retorno + 'grafico_inicio'
      } else 
      if (classe == "INICIOFIM") {
        return retorno + 'grafico_inicio grafico_fim'
      } else 
      if (classe == "INICIOINDO") {
        return retorno + 'grafico_inicio_indo'
      } else 
      if (classe == "INICIODURANTE") {
        return retorno + 'grafico_inicio_durante'
      } else 
      if (classe == "INICIOBAIXADO") {
        return retorno + 'grafico_inicio_baixado'
      } else 
      if (classe == "DURANTE") {
        return retorno + 'grafico_durante'
      } else 
      if (classe == "DURANTEINDO") {
        return retorno + 'grafico_durante_indo'
      } else 
      if (classe == "DURANTEBAIXADO") {
        return retorno + 'grafico_durante_baixado'
      } else 
      if (classe == "DURANTEFIM") {
        return retorno + 'grafico_fim'
      } else 
      if (classe == "FIM") {
        return retorno + 'grafico_fim'
      } else
      if (classe == "INDO") {
        return retorno + 'grafico_indo'
      } else      
      if (classe == "VINDO") {
        return retorno + 'grafico_vindo'
      } else
      if (classe == "INDOVINDO") {
        return retorno + 'grafico_indo grafico_vindo'
      } else
      if (classe == "VINDOINDO") {
        return retorno + 'grafico_vindo_indo'
      } else
      if (classe == "VINDOFIM") {
        //return retorno + 'grafico_vindo grafico_fim'
        return retorno + 'grafico_vindo_fim'
      } else
      if (classe == "VINDODURANTE") {
        return retorno + 'grafico_vindo_durante'
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

    newColorStatusItem(status){
      if (status == 'ABERTA'){
        return 'bg-aberta' // '#FFF9C4' teal darken-2
      } else if (status == 'ENCERRADA'){
        return 'bg-encerrada' //'#d3dce6' // blue-grey lighten-1
      } else if (status == 'VENCIDA'){
        return 'bg-vencida' // '#FFCCBC' red accent-4
      }
      return 'blue'
    },

    colorStatusItem(status){
      if (status == 'ABERTA'){
        return '#FFF9C4' // teal darken-2
      } else if (status == 'ENCERRADA'){
        return '#d3dce6' //'#E0E0E0' // blue-grey lighten-1
      } else if (status == 'VENCIDA'){
        return '#FFCCBC' // red accent-4
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

    newCalcularAlturaLeito(qtd){
      if (qtd <= 1) {
        return '28px'
      } else {
        return  (qtd*28)+'px'
      }
    },

    getHospedagemById(id){
      return _.find(this.dados.hospedagens,{identificador : id});
    },

    getHospedagensByLeitoId(id){
      var hospedagensQuadro = []
      var quadroQuartoNumero = null
      var quadroLeitoNumero = null

      var diaIndex = this.diaIndex

      _.forEach(this.dados.pessoas, function(v, k){
        var lst = _.filter(v.leitos, {leitoId : id})

        for (var i = 0; i < lst.length; i++){
          var hospedagem = lst[i];

          //TODO OTIMIZAR. DEVE PROCURAR PELA LISTA DE LEITOS

          const zeroOuUm = hospedagem.dias[diaIndex]
          if (zeroOuUm == 1){
            var retorno = v
            ///////////retorno.leitos = undefined
            retorno.hospedagem = hospedagem

            quadroQuartoNumero = hospedagem.quartoNumero
            quadroLeitoNumero = hospedagem.leitoNumero

            hospedagensQuadro.push(retorno)
          }
        }

        //console.log(v)
      })
      this.hospedagensQuadro = hospedagensQuadro
      this.quadroQuartoNumero = quadroQuartoNumero
      this.quadroLeitoNumero = quadroLeitoNumero

      //console.log(this.hospedagensQuadro)
      return this.hospedagensQuadro
    },

    getPrimeiroNome(nome){
      if (nome.length > 10){
        nome = nome.substr(0,10) + "..."
      }
      return nome;
    },

    showHospedagemInfo(id){
      this.state = "info"
      this.hospedagemSelecionada = id;
      this.showTelaHospedagem = true;
    },

    onCloseInfo(){
      this.state = "browse"
      this.hospedagemSelecionada = null;
      this.$store.dispatch('setAcao','Hospedagens')
      this.refreshMapa()
    },

		setShowDate(d) {
			this.message = `Changing calendar view to ${d.toLocaleDateString()}`
      this.showDate = d
      this.dataAtual = moment(d).format("YYYY-MM-DD")
    },
    
		onDrop(event, date) {
			this.message = `You dropped ${event.id} on ${date.toLocaleDateString()}`
			const eLength = this.dayDiff(event.startDate, date)
			event.originalEvent.startDate = this.addDays(event.startDate, eLength)
			event.originalEvent.endDate = this.addDays(event.endDate, eLength)
		},

  }
}
</script>

<style scoped>

  .block-date {
    display:flex;
    margin-left:10px;
    padding-left:4px;
  }

  .el-card__header {
    padding: 5px;
  }

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

.fonte-branca{
  color: white;
}

.grade {
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

/*
.thebox2 {
  box-sizing: border-box;
  border-radius: 0px;
  margin: 0px;
  height: 30px;
  border: 1px #bdbdbd dashed;
}
*/

.thebox-circular {
  box-sizing: border-box;
  border-radius: 25px;
  margin: 0px;
  height: 30px;
  margin-left: 3px;
  margin-right: 3px;
  margin-bottom: 3px;
  padding-top:5px;
  border: 1px #bdbdbd solid;
  font-size: 10pt;
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

.cinza {
  background-color: #d3dce6;
}

.grafico{
  margin-top: 1px;
  margin-bottom:1px;
  margin-left:-1px;
  margin-right:-1px;
  margin-top: 0px;
  cursor:pointer;
  padding: 0px;
}

.grafico_inicio {
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
  width: calc(100% - 2px);
  margin-left: 3px;
}

.grafico_inicio_durante {
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
  width: calc(100% - 2px);
  margin-left: 3px;
}

.grafico_fim {
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
  width: calc(100% - 3px);
}

/*  https://css-tricks.com/the-shapes-of-css/ */
.grafico_inicio_indo {
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
  width: calc(100% - 6px);
  margin-left: 3px;

  border-color:#000;
  border-style: dotted;
  border-width: 0px 4px 0px 0px;
}

.grafico_inicio_baixado {
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
  width: calc(100% - 6px);
  margin-left: 3px;

  border-color:green;
  border-style: dotted;
  border-width: 0px 4px 0px 0px;
}

.grafico_indo {
  border-color:#000;
  border-style: dotted;
  border-width: 0px 4px 0px 0px;
}

.grafico_vindo_indo {
  border-color:#000;
  border-style: dotted;
  border-width: 0px 4px 0px 4px;
}

.grafico_durante_indo{
  border-color:#000;
  border-style: dotted;
  border-width: 0px 4px 0px 0px;
}

.grafico_fim_baixado {
  border-color:#000;
  border-style: solid;
  border-width: 0px 4px 0px 0px;
}

.grafico_durante_baixado {
  border-color:#000;
  border-style: solid;
  border-width: 0px 4px 0px 0px;
}

.grafico_vindo {
  border-color:#000;
  border-style: dotted;
  border-width: 0px 0px 0px 4px;
}

.grafico_vindo_durante {
  border-color:#000;
  border-style: dotted;
  border-width: 0px 0px 0px 4px;
}

.grafico_vindo_fim {
  border-color:#000;
  border-style: dotted;

  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
  width: calc(100% - 6px);

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

.chip{
  font-size:8pt;
  margin-left:4px;
  background-color:white;
  width:88px;
  text-align:center;
  border: 1px solid #bbb;
  border-radius: 16px;
}

.chip p{
  margin:0;
  padding-top:4px;
}

.flex-container {
  padding: 0;
  margin: 0;
  list-style: none;
  box-orient: horizontal;
  display: flex;
  justify-content: space-around;
}

.wrap    { 
  flex-wrap: wrap;
}  

.wrap li {
  background: gold;
}

.flex-item {
  background: #FFF9C4;
  color: #455A64;
  padding: 2px;
  width: 300px;
  margin: 10px;
}

.texto-pq{
  font-size: 10pt;
}

.subtitulo {
  margin:0 10px;
  padding:5px;
  text-align: center;
  border-radius: 4px;
}

.bg-purple {
  background: #d3dce6;
}

.bg-aberta{
    background: #4DB6AC;
}

.bg-encerrada {
  background: #B0BEC5; /*#b0bec5;*/
}

.bg-vencida {
  background: #FF8A80;
}

.container-mapa {
  display: grid;
  grid-template-columns: 1fr 7fr;
}

.container-linha {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  font-size: 8pt;
}

.container-mapa .container-linha div{
  height: 28px;
}

.container-mapa div.titulo {
  display:grid;
  border: 1px solid #CFD8DC;
}

.celula-borda-cinza {
  border: 1px solid #CFD8DC;
}

.texto-linha{
  margin:0; 
  padding: 5px 10px;
  color:#263238; 
  font-size:9pt;
}


/* ------------------------------------------------------------- */

table {
  font-size: 9pt;
  border: 1px solid #CFD8DC;
  border-radius: 3px;
  background-color: #fff;
  border-collapse: collapse;
}

th {
  background-color: #42b983;
  color: rgba(255,255,255,0.66);
  border: 1px solid #CFD8DC;
  cursor: pointer;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

td {
  background-color: #f9f9f9;
  border: 1px solid #CFD8DC;
}

th, td {
  padding: 8px 10px;
}

th.active {
  color: #fff;
}

th.active .arrow {
  opacity: 1;
}

.arrow {
  display: inline-block;
  vertical-align: middle;
  width: 0;
  height: 0;
  margin-left: 5px;
  opacity: 0.66;
}

.arrow.asc {
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  border-bottom: 4px solid #fff;
}

.arrow.dsc {
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  border-top: 4px solid #fff;
}





</style>