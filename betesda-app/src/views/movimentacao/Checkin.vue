<template>
  <div>

    <el-container v-if="state == 'browse'">
      <el-header>
        <el-tooltip content="Limpar infomações" placement="bottom" :open-delay="toolTipDelay">
          <el-button type="primary" @click="handleInsert">Limpar</el-button>
        </el-tooltip>
        <el-tooltip content="Insere nova Hospedagem com as informações" placement="bottom" :open-delay="toolTipDelay">
          <el-button type="primary" @click="handleInsert">Lançar Hospedagema</el-button>
        </el-tooltip>
      </el-header>

      <el-main style="padding-top:0px; padding-left:5px; padding-right:5px; padding-bottom:5px;">
        <el-form :model="form" :rules="rules" ref="form" label-position="left" size="small" label-width="100px">
        <el-row type="flex" justify="center">
          <el-col :xs="24" :sm="24" :md="12" :lg="12">
            <el-container>
              <el-header style="padding-left:0; padding:5px; line-height:40px;">
                <el-row>
                  <el-col>
                    <div class="subtitulo bg-purple">OPÇÕES</div>
                  </el-col>
                </el-row>
              </el-header>
              <el-main style="padding: 0px 5px;">
                <el-row :gutter="10">
                  <el-col :span="24">
                    <el-form-item label="Entidade" prop="entidadeId" :error="getErro('entidadeId')">
                      <el-select v-model="form.entidadeId" ref="entidadeId" style="width: 100%;" size="mini">
                        <el-option v-for="item in itensEntidades" :key="item.value" :label="item.text" :value="item.value"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="10">
                  <el-col :span="24">
                    <el-form-item label="Encaminhador" prop="encaminhadorId" :error="getErro('encaminhadorId')">
                      <el-select v-model="form.encaminhadorId" style="width: 100%;" size="mini">
                        <el-option v-for="item in itensEncaminhadores" :key="item.value" :label="item.text" :value="item.value"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="10">
                  <el-col :span="12">
                    <el-form-item label="Dt.Entrada" prop="dataNascimento" :error="getErro('dataNascimento')">
                      <el-date-picker type="date" format="dd/MM/yyyy" value-format="yyyy-MM-dd" style="width: 100%;" size="mini"></el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="Prev. Saída" prop="dataNascimento" :error="getErro('dataNascimento')">
                      <el-date-picker type="date" format="dd/MM/yyyy" value-format="yyyy-MM-dd" style="width: 100%;" size="mini"></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="10">
                  <el-col :span="12">
                    <el-form-item label="Destinação" prop="destinacaoHospedagemId" :error="getErro('destinacaoHospedagemId')">
                      <el-select v-model="form.destinacaoHospedagemId" style="width: 100%;" size="mini">
                        <el-option v-for="item in itensDestinacaoHospedagem" :key="item.value" :label="item.text" :value="item.value"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="Utilização" prop="tipoUtilizacao" :error="getErro('tipoUtilizacao')">
                      <el-select v-model="form.tipoUtilizacao" style="width: 100%;" size="mini">
                        <el-option v-for="item in itensUtilizacao" :key="item.value" :label="item.text" :value="item.value"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="10" v-if="(form.tipoUtilizacao == 'P')">
                  <el-col :span="24">
                    <el-form-item label="Serviços" prop="destinacoes" :error="getErro('destinacoes')">
                      <el-select v-model="form.servicos" multiple placeholder="Selecione um ou mais" style="width:100%">
                        <el-option
                          v-for="item in itensTipoServico"
                          :key="item.value"
                          :label="item.text"
                          :value="item.value">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="10">
                  <el-col :span="24">
                    <el-form-item label="Observacoes" prop="fixme" :error="getErro('observacoes')">
                      <el-input type="textarea" rows="5" v-model="form.observacoes" size="mini"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>


              </el-main>
            </el-container>
          </el-col>

          <el-col :xs="24" :sm="24" :md="12" :lg="12">

            <el-container>
              <el-header style="padding-left:0; padding:5px; line-height:40px; ">
                <el-row>
                  <el-col>
                    <div class="subtitulo bg-purple">HÓSPEDES</div>
                  </el-col>
                </el-row>
              </el-header>
              <el-main style="padding: 0px 5px;">
                <el-row>

                  <el-card body-style="card-body-style" v-for="(item, i)  in aguardando" :key="i" shadow="never" >

                    <div slot="header" class="clearfix" >
                      <span class="card-header">{{item.pessoa.nome}}</span>

                      <el-tooltip content="Remover da Hospedagem" placement="bottom" :open-delay="toolTipDelay">
                          <el-button @click="removerHospede(item)" style="float: right; margin-left:10px" plain size="small" circle ><i class="fas fa-trash"></i></el-button>
                      </el-tooltip>

                      <el-tooltip v-if="form.tipoUtilizacao == 'T'" content="Selecionar Leito" placement="bottom" :open-delay="toolTipDelay">
                          <el-button @click="selecionarLeito(item)" style="float: right; margin-left:10px" plain size="small" circle ><i class="fas fa-bed"></i></el-button>
                      </el-tooltip>

                      <el-tooltip content="Alterar Tipo de Hóspede" placement="bottom" :open-delay="toolTipDelay">
                          <el-button @click="selecionarTipoHospede(item)" style="float: right; margin-left:10px" plain size="small" circle ><i class="fas fa-users"></i></el-button>
                      </el-tooltip>

                      <el-tooltip content="Conferir/Editar dados do Hóspede" placement="bottom" :open-delay="toolTipDelay">
                          <el-button @click="editarHospede(item)" style="float: right; margin-left:10px" plain size="small" circle ><i class="fas fa-pencil-alt"></i></el-button>
                      </el-tooltip>
                    </div> 

                    <div class="card-detail">
                      <div v-if="(item.tipoHospede == null)" class="red-lighten-4">Selecione o Tipo de Hóspede</div> 
                      <div v-if="(item.tipoHospede != null)">{{item.tipoHospede.descricao}}</div>

                      <div v-if="(form.tipoUtilizacao == 'T') && (item.acomodacao == null)" class="red-lighten-4">Selecione o Leito</div> 
                      <h4 v-if="item.acomodacao != null">Quarto: {{item.acomodacao.quarto.numero}} Leito: {{item.acomodacao.leito.numero}}</h4>
                    </div>

                  </el-card>
                </el-row>


              </el-main>
            </el-container>

          </el-col>
        </el-row>
        </el-form>
      </el-main>
    </el-container>


    <el-container v-if="state != 'browse'">
      <el-header>
        <el-row type="flex">  
          <el-tooltip content="Gravar alterações" placement="bottom" :open-delay="toolTipDelay">
            <div style="margin-right:10px;"><el-button type="primary" @click="handleSave">Gravar</el-button></div>
          </el-tooltip>
          <el-tooltip content="Cancelar alterações" placement="bottom" :open-delay="toolTipDelay">
            <div style="margin-right:10px;"><el-button @click="handleCancel">Cancelar</el-button></div>
          </el-tooltip>
          <div>{{state=='edit' ? 'Editando Tipo de Serviço' : 'Incluindo Tipo de Serviço'}}</div>
        </el-row>
      </el-header>
      <el-main>
        
        <el-row class="container" type="flex" justify="center" align="middle">
          <el-col :sm="24" :md="24" :lg="18">

              <el-form :model="form" :rules="rules" ref="form"
                label-position="top" size="small" label-width="140px">

                <el-form-item label="Descrição" prop="descricao" :error="getErro('descricao')">
                  <el-input v-model="form.descricao" ref="edtdescricao"></el-input>
                </el-form-item>

                <el-form-item label="Ativo?" prop="ativo" :error="getErro('ativo')">
                  <el-select v-model="form.ativo">
                    <el-option label="SIM" value="S"></el-option>
                    <el-option label="NÃO" value="N"></el-option>
                  </el-select>
                </el-form-item>

              </el-form>
          </el-col>
        </el-row>

      </el-main>
    </el-container>

    <el-dialog
      title="Confirmação"
      :visible.sync="dialogExclusaoVisible"
      width="600px">
      <span>{{textToDelete}}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleConfirmDelete(false)">Cancelar</el-button>
        <el-button type="primary" @click="handleConfirmDelete(true)">Sim</el-button>
      </span>
    </el-dialog>

  </div>

</template>

<script>

export default {

  name: 'Checkin',

  created(){
  },

  mounted(){
    this.$store.dispatch('setAcao','Checkin')
    this.loadListas()
      this.$nextTick(() => {
        setTimeout(() => this.$refs.entidadeId.focus(), 500)
      })      
  },

  data: () =>({
    dados: [],

    erros: [],

    hospedes: [],

    itensDestinacaoHospedagem : [],
    itensTipoHospede : [],
    itensEntidades : [],
    itensEncaminhadores : [],
    itensUtilizacao: [{ text: "Total", value: "T" }, { text: "Parcial", value: "P" }],


    state : 'browse',

    dialogExclusaoVisible : false,

    idToDelete : null,
    textToDelete : null,

    form : {
      entidadeId: null,
      encaminhadorId: null,
      dataEntrada: null,    
      dataPrevistaSaida: null,
      destinacaoHospedagem: null,
      tipoUtilizacao: null,
      servicos: [],
      observacoes : null
    },

    rules: {
      descricao: [
        { required: true, message: "Preencha a descrição",trigger: "blur"},
        { min: 3, max: 32, message: "Descrição deve ter de 3 a 32 caracteres",trigger: "blur"}
      ],
    },

    toolTipDelay: 500

  }),

  computed: {

    destinacaoHospedagem(){
      return this.form.destinacaoHospedagem
    },

    tipoUtilizacao(){
      return this.form.tipoUtilizacao
    },

    entidadeId(){
      return this.form.entidadeId
    },

    itensSelecaoTipoHospede(){
      return this.itensTipoHospede
    },

    servicos(){
      return this.form.servicos
    },

    aguardando(){
      return this.$store.getters.aguardando;
    },

  },


  watch : {
    destinacaoHospedagem(){
      //this.loadQuartosPorTipoUtilizacao(this.formOpcoes.destinacaoHospedagem)
      this.clearLeitos()
    },

    tipoUtilizacao(value){
      if (value == "P"){
        //////////////////////////////////this.clearLeitos()
      }
    },

    entidadeId(newValue, oldValue){
      //console.logconsole.log(`mudou de ${oldValue} para ${newValue}`)
      if (newValue == null){
        this.itensEncaminhadores = []
      } else {
        this.loadEncaminhadores(newValue)
      }
    },

    /*
    servicos(){
    },


    estado(value){
      if (value == 'incluindo'){
        this.focus()
      }
    }
    */
  },


  methods: {

    resetData(){

      this.form = {
        entidadeId: null,
        encaminhadorId: null,
        dataEntrada: null,    
        dataPrevistaSaida: null,
        destinacaoHospedagem: null,
        tipoUtilizacao: null,
        servicos: [],
        observacoes : null
      },
      this.hospedes = []
      this.errors = []
      this.hospedagemGravadaId = 0
      this.itensEncaminhadores = []
      this.itensEntidades = []
    },

    setDefaultData(){
      this.form.ativo = 'S'
    },

    fmtSimNao(row, col){
      return (row.ativo == 'S' ? 'Sim' : 'Não')
    },

    getErro(campo){
      var retorno =  _.find(this.erros,{fieldName : campo})
      if (retorno){
        return retorno.errorMessage
      }
      return null
    },

    loadListas() {
      this.itensDestinacaoHospedagem = [];
      petra.axiosGet("/app/quarto/listas",false).then(
        response => {
          this.itensDestinacaoHospedagem = response.data.listaDestinacaoHospedagem
          this.itensTipoLeito = response.data.listaTipoLeito
          this.itensSituacaoLeito = response.data.listaSituacaoLeito
          this.itensTipoServico = response.data.listaTipoServico
          this.itensTipoHospede = response.data.listaTipoHospede
          this.itensEntidades = response.data.listaEntidade
        })
    },

    loadEncaminhadores(id) {
      this.itensEncaminhadores = [];
      var endpoint = "/app/encaminhadores/lista/"+id
      petra.axiosGet("/app/encaminhadores/lista/"+id).then(
        response => {
          this.itensEncaminhadores = response.data
        })
    },

    clearLeitos(){
      for (var i = 0; i < this.hospedes.length; i++) {
        this.hospedes[i].acomodacao = null
      }      
      this.rerender()
    },

    onSelecionarPessoa(_pessoa, _tipoHospede){
      if (_pessoa != null) {
        for (var i = 0; i < this.hospedes.length; ++i){
          if (_pessoa.id == this.hospedes[i].pessoa.id){
            return
          }
        }
        var hospede = {
          pessoa : _pessoa,
          tipoHospede : _tipoHospede,
          acomodacao : null // {quarto, leito}
        }
        //console.log("Selecionando pessoa: ", _pessoa)
        this.hospedes.push(hospede)
      }
    },

    handleEdit(row){
      this.resetData()
      this.doGetById(row.id)
    },

    handleInsert(){
      this.state = "insert"
      this.resetData()
      this.setDefaultData()
      this.$nextTick(() => {
        setTimeout(() => this.$refs.edtdescricao.focus(), 500)
      })      
    },
    
    handleSave(){
      this.doSave()
    },
    
    handleDelete(row){
      this.textToDelete = `Deseja realmente excluir o Tipo de Serviço "${row.descricao}"?`
      this.idToDelete = row.id
      this.dialogExclusaoVisible = true
    },

    handleCancel(row){
      this.state = "browse"
      this.doGetAll()
    },

    handleConfirmDelete(confirm) {
      this.dialogExclusaoVisible = false
      if (confirm){
        this.doDelete()
      }
    },

    doGetAll(evt) {
      petra.axiosGet("/app/tipo_servico").then(
        response => {
          this.dados = response.data
        })
    },

    doGetById(id) {
      petra.axiosGet(`/app/tipo_servico/${id}`).then(
        response => {
          this.form = response.data
          this.state = "edit"
          this.$nextTick(() => {
            setTimeout(() => {
              this.$refs.edtdescricao.focus()
            }, 500)
          })      
        })
    },

    doDelete(evt) {
      petra.axiosDelete("/app/tipo_servico/"+this.idToDelete)
        .then(response => {
          petra.showMessageSuccess('Tipo de Serviço excluído com sucesso')
          this.doGetAll()
        })
        .catch(error => {
          petra.tratarErros(error)
        })
    },

    doSave(evt) {
      this.errors = [];

      petra.axiosPost("/app/tipo_servico/", this.form, false)
        .then(response => {
          petra.showMessageSuccess('Tipo de Serviço gravado com sucesso')
          this.state="browse"
          this.doGetAll()
        })
        .catch(error => {
          this.erros = petra.tratarErros(error)
        })
    },

    removerHospede(item){
      /*
      var indice = this.hospedes.indexOf(item);
      if (indice > -1){
        this.hospedes.splice(indice,1)
      }
      */
    },

    selecionarLeito(hospede) {
      /*
      this.selecionarAcomodacao(hospede)
      this.$refs.dlgSelecaoLeito.openDialog(hospede, this.formOpcoes.destinacaoHospedagem, this.dataEntrada, this.dataPrevistaSaida)
      */
    },

    selecionarTipoHospede(hospede) {
      /*
      this.hospedeSelecionado = hospede
      this.$refs.dlgSelTipoHospede.openDialog(hospede.pessoa.nome)
      */
    },

    onCloseSelecaoTipoHospede(tipoHospede){
      /*
      if (tipoHospede) {
        if (this.hospedeSelecionado != null){
          this.hospedeSelecionado.tipoHospede = tipoHospede
          this.rerender()
        }
      }
      */
    },

    editarHospede(hospede) {
      /*
      this.$refs.dlgPessoaEdit.openDialog(hospede.pessoa)
      */
    },

    onSelecionarPessoa(_pessoa, _tipoHospede){
      /*
      if (_pessoa != null) {
        for (var i = 0; i < this.hospedes.length; ++i){
          if (_pessoa.id == this.hospedes[i].pessoa.id){
            return
          }
        }
        var hospede = {
          pessoa : _pessoa,
          tipoHospede : _tipoHospede,
          acomodacao : null // {quarto, leito}
        }
        //console.log("Selecionando pessoa: ", _pessoa)
        this.hospedes.push(hospede)
      }
      */
    },

    onUpdatePessoa(_pessoa){
      /*
      if (_pessoa != null) {
        for (var i = 0; i < this.hospedes.length; ++i){
          if (_pessoa.id == this.hospedes[i].pessoa.id){
            this.this.hospedes[i].pessoa = _pessoa
            return
          }
        }
      }
      */
    },

    onCloseSelecionarPessoa(){
      //this.focus()
    },


    postarHospedagem() {
      this.errors = []

      var toSave = {
        entidadeId : this.formOpcoes.entidadeId,
        encaminhadorId : this.formOpcoes.encaminhadorId,
        dataEntrada : this.dataEntrada,
        dataPrevistaSaida : this.dataPrevistaSaida,
        destinacaoHospedagemId : this.formOpcoes.destinacaoHospedagem,
        tipoUtilizacao : this.formOpcoes.tipoUtilizacao,
        observacoes : this.formOpcoes.observacoes,
        servicos : [],
        hospedes : []

      }
      if (this.formOpcoes.tipoUtilizacao == "P") {
        toSave.servicos = this.formOpcoes.servicos
      }
      for (var i = 0; i < this.hospedes.length; ++i){
        var hospede = {
          pessoaId : this.hospedes[i].pessoa.id,
          pessoaNome : this.hospedes[i].pessoa.nome,
          tipoHospedeId : this.hospedes[i].tipoHospede.id,
        }

        if ((this.formOpcoes.tipoUtilizacao == "T") && (this.hospedes[i].acomodacao != null)){ 
          hospede.acomodacao = {
            quartoId : this.hospedes[i].acomodacao.quarto.id,
            quartoNumero : this.hospedes[i].acomodacao.quarto.numero,
            leitoId : this.hospedes[i].acomodacao.leito.id,
            leitoNumero : this.hospedes[i].acomodacao.leito.numero,
          }
        }
        toSave.hospedes.push(hospede)
      }

      petra.axiosPost("/app/hospedagem", toSave)
        .then(response => {
            this.errors = []
            this.resetHospedagem()
            this.showHospedagemGravada(response.data.id)
        })
        .catch(error => {
          this.errors = petra.tratarErros(error)
        })
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

  .subtitulo {
    text-align: center;
    border-radius: 4px;
  }

  .bg-purple {
    background: #d3dce6;
  }

  .card-body-style {
    padding : 10px;
    color: red;
  }

  .card-header{
    font-size: 10pt;
    font-weight: bold;
  }

  .card-detail{
    font-size: 10pt;
  }

  .red-lighten-4{
    padding:2px;
    background: #FFCDD2;
  }

</style>
