<template>
  <div>
    <el-container v-if="state != 'browse'">
      <el-header>
        <el-row type="flex">  
          <div style="margin-right:10px;"><el-button type="primary" @click="getPlanilhaGeral">Gerar Planilha</el-button></div>
        </el-row>
      </el-header>
      <el-main>
        
        <el-row class="container" type="flex" justify="center" align="middle">
          <el-col :sm="24" :md="24" :lg="18">
              <el-form :model="form" :rules="rules" ref="form"
                label-position="top" size="small" label-width="140px">
                <el-form-item label="Data Inicial" prop="dataInicial" :error="getErro('dataInicial')">
                  <el-date-picker v-model="form.dataInicial" type="date" format="dd/MM/yyyy" value-format="yyyy-MM-dd" ref="edtdatainicial" style="width: 100%;" ></el-date-picker>
                </el-form-item>
                <el-form-item label="Data Final" prop="dataFinal" :error="getErro('dataFinal')">
                  <el-date-picker v-model="form.dataFinal" type="date" format="dd/MM/yyyy" value-format="yyyy-MM-dd" style="width: 100%;" ></el-date-picker>
                </el-form-item>
              </el-form>
          </el-col>
        </el-row>

      </el-main>
    </el-container>

    <el-container v-if="state == 'browse'">
      <el-header>
        <el-row type="flex">  
          <el-button type="primary" @click="handleSelect">Selecionar Período</el-button>
          <el-button type="primary" @click="excel">Download da Planilha</el-button>
          <div style="margin-left:10px; line-height:1em;">Período: {{periodoSelecionado()}}</div>
        </el-row>
      </el-header>
      <el-main>
        <el-row type="flex" justify="center" align="middle">
          <el-col :sm="24" :md="24" :lg="24">

            <el-tabs type="border-card" v-model="activeTabName" @tab-click="handleTabClick">

              <el-tab-pane label="Movimentação" name="movimentacao">
                <el-table :data="dadosPlanilhaGeral" 
                  style="width: 100%" border size="small" 
                  :default-sort="{prop: 'pessoaNome', order: 'ascending'}"
                  :height="tableHeight">

                  <el-table-column prop="hospedagemId" label="Atendimento" fixed align="right" width="90" ></el-table-column>

                  <el-table-column prop="pessoaId" label="Pessoa" fixed width="100" align="right" sortable></el-table-column>
                  <el-table-column prop="pessoaNome" label="Nome" fixed width="300" sortable></el-table-column>

                  <el-table-column prop="pessoaDataNascimento" :formatter="fmtDateFull" label="Nascimento" width="120"></el-table-column>
                  <el-table-column prop="pessoaIdade" label="Idade" align="right" width="80" header-align="left"></el-table-column>
                  <el-table-column prop="pessoaFaixaEtaria" label="Faixa Etária" width="120"></el-table-column>

                  <el-table-column prop="tipoUtilizacaoDescricao" label="Tipo" width="120" ></el-table-column>

                  <el-table-column prop="encaminhadorNome" label="Encaminhador" sortable width="300" ></el-table-column>


                  <el-table-column prop="pessoaCPF" label="CPF" width="140"></el-table-column>
                  <el-table-column prop="pessoaRG" label="RG" width="140"></el-table-column>

                  <el-table-column prop="pessoaTelefone" label="Telefone" width="140"></el-table-column>

                  <el-table-column prop="pessoaEndereco" label="Endereço" width="500"></el-table-column>
                  <el-table-column prop="pessoaCidadeOrigem" label="Cidade de Origem" width="200"></el-table-column>
                  <el-table-column prop="pessoaCidadeOrigemUF" label="UF Origem" width="80"></el-table-column>

                  <el-table-column prop="tipoHospede" label="Tipo de Hospedagem" width="140" ></el-table-column>

                  <el-table-column prop="dataEntrada" :formatter="fmtDate" label="Dt. Ingresso" width="140" header-align="left" sortable></el-table-column>
                  <el-table-column prop="dataSaida" :formatter="fmtDate" label="Dt. Desligamento" width="140" class-name="wordwrap" sortable></el-table-column>

                  <el-table-column prop="diasPermanencia" label="Dias" align="right" width="90" ></el-table-column>
                </el-table>
              </el-tab-pane>

              <el-tab-pane label="Ranking de Cidades" name="cidades">
                <el-table :data="dadosRankingCidades" 
                  style="width: 100%" border size="small" 
                  :default-sort="{prop: 'quantidade', order: 'descending'}"
                  :height="tableHeight">

                  <el-table-column prop="cidade" label="Cidade" sortable width="300"></el-table-column>
                  <el-table-column prop="uf" label="UF" width="80"></el-table-column>
                  <el-table-column prop="quantidade" label="Atendimentos" align="right" sortable width="150" ></el-table-column>
                </el-table>
              </el-tab-pane>

              <el-tab-pane label="Ranking de Encaminhadores" name="encaminhadores">

                <el-table :data="dadosRankingEncaminhamentos" 
                  style="width: 100%" border size="small" 
                  :default-sort="{prop: 'quantidade', order: 'descending'}"
                  :height="tableHeight">

                  <el-table-column prop="nome" label="Encaminhador" sortable width="400"></el-table-column>
                  <el-table-column prop="quantidade" label="Atendimentos" sortable align="right" width="150" ></el-table-column>
                </el-table>

              </el-tab-pane>
            </el-tabs>

          </el-col>
        </el-row>
      </el-main>
    </el-container>

  </div>

</template>

<script>

import XLSX from 'xlsx'

export default {

  name: 'PlanilhaGeral',

  created(){
    this.form.dataInicial = petraDateTime.primeiroDiaMesAnterior()
    this.form.dataFinal = petraDateTime.ultimoDiaMesAnterior()
  },

  mounted(){
    this.$store.dispatch('setAcao','Planilha Geral')

    this.windowHeight = window.innerHeight

    this.$nextTick(() => {
      window.addEventListener('resize', () => {
        this.windowHeight = window.innerHeight
        this.tableHeight = window.innerHeight - 200
      })
    })

  },

  data: () =>({
    dados: [],

    erros: [],

    state : 'edit',

    activeTabName: 'movimentacao',

    //dialogExclusaoVisible : false,

    //idToDelete : null,
    //textToDelete : null,

    form : {
      dataInicial : null,
      dataFinal : null
    },

    dadosPlanilhaGeral:[],
    dadosRankingCidades:[],
    dadosRankingEncaminhamentos:[],

    windowHeight: 0,
    tableHeight : window.innerHeight - 270,

    rules: {
      dataInicial: [
        { required: true, message: "Data Inicial deve ser preenchida",trigger: "blur"},
      ],
      dataFinal: [
        { required: true, message: "Data Final deve ser preenchida",trigger: "blur"},
      ],
    },

    toolTipDelay: 500

  }),

  watch: {
    windowHeight(newHeight, oldHeight) {
      //this.txt = `mudou de ${oldHeight} para ${newHeight}`
      //this.styleGrid = `max-height: ${newHeight - 272}px`
      //this.styleContainerMapa = `height: ${newHeight-270}px`
      this.tableHeight = `${newHeight-270}`
    }    
  },  

  methods: {

    resetData(){
      this.form = {
        dataInicial : null,
        dataFinal : null
      }
      this.erros = []
    },

    fmtDate(row, col, cellValue, index){
      return petraDateTime.formatDate(cellValue, "DD/MM");
    },

    fmtDateFull(row, col, cellValue, index){
      return petraDateTime.formatDate(cellValue, "DD/MM/YYYY");
    },

    getErro(campo){
      var retorno =  _.find(this.erros,{fieldName : campo})
      if (retorno){
        return retorno.errorMessage
      }
      return null
    },

    handleTabClick(tab, event) {
    },

    handleEdit(row){
      this.resetData()
      this.doGetById(row.id)
    },

    handleSelect(){
      this.state = "insert"
      this.resetData()
      //this.setDefaultData()
      this.$nextTick(() => {
        setTimeout(() => this.$refs.edtdatainicial.focus(), 500)
      })      
    },

    periodoSelecionado(){
      const dataInicial = petraDateTime.formatDate(this.form.dataInicial, "DD/MM/YYYY"); 
      const dataFinal = petraDateTime.formatDate(this.form.dataFinal, "DD/MM/YYYY"); 
      return dataInicial + " até " + dataFinal
    },

    handleSave(){
      this.doSave()
    },
    
    getPlanilhaGeral(){
      this.getDadosPlanilhaGeral(this.form.dataInicial, this.form.dataFinal)
    },

    getDadosPlanilhaGeral(dataIni, dataFim) {
      var dados = {
        dataIni : dataIni,
        dataFim : dataFim
      }
      petra.axiosPost("/app/hospedagem/planilha_geral", dados)
        .then(response => {
          this.dadosPlanilhaGeral = response.data.planilhaGeral
          this.dadosRankingCidades = response.data.rankingCidades;
          this.dadosRankingEncaminhamentos = response.data.rankingEncaminhamentos;

          console.log(response.data)
          this.state="browse"
        })
        .catch(error => {
          this.erros = petra.tratarErros(error)
        })
    },

    excel() { // On Click Excel download button
      /*
      var animals = [
                  {"name": "cat", "category": "animal"}
                  ,{"name": "dog", "category": "animal"}
                  ,{"name": "pig", "category": "animal"}
                ]

      var pokemons = [
                  {"name": "pikachu", "category": "pokemon"}
                  ,{"name": "Arbok", "category": "pokemon"}
                  ,{"name": "Eevee", "category": "pokemon"}
                ]
      */
    
      // export json to Worksheet of Excel
      // only array possible
      var dadosPlanilhaGeralWS = XLSX.utils.json_to_sheet(this.dadosPlanilhaGeral) 
      var dadosRankingCidadesWS = XLSX.utils.json_to_sheet(this.dadosRankingCidades) 
      var dadosRankingEncaminhamentosWS = XLSX.utils.json_to_sheet(this.dadosRankingEncaminhamentos) 




      //var animalWS = XLSX.utils.json_to_sheet(animals) 
      //var pokemonWS = XLSX.utils.json_to_sheet(pokemons) 

      // A workbook is the name given to an Excel file
      var wb = XLSX.utils.book_new() // make Workbook of Excel

      // add Worksheet to Workbook
      // Workbook contains one or more worksheets
      XLSX.utils.book_append_sheet(wb, dadosPlanilhaGeralWS, 'Movimentacao') // sheetAName is name of Worksheet
      XLSX.utils.book_append_sheet(wb, dadosRankingCidadesWS, 'Ranking de Cidades') // sheetAName is name of Worksheet
      XLSX.utils.book_append_sheet(wb, dadosRankingEncaminhamentosWS, 'Ranking de Encaminhadores')   

      // export Excel file
      XLSX.writeFile(wb, 'atendimentosNoMes.xlsx') // name of the file is 'book.xlsx'
    },


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

</style>
