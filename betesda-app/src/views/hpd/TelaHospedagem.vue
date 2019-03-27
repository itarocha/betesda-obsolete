<template>
  <div>
    <listagem-erros :errors="errors"></listagem-erros>
    <el-container v-if="state == 'browse'">
      <el-header>
          <el-button type="primary" @click="showSelecionarDataEncerramento" v-if="permitirEncerrar">Encerrar</el-button>
          <el-button type="primary" @click="showSelecionarDataRenovacao" v-if="permitirRenovar">Renovar</el-button>
          <el-button type="primary" @click="showSelecionarAcrescentarHospede" v-if="permitirAcrescentar">Acrescentar Hóspede</el-button>
          <el-button type="danger" @click="showConfirmarExclusao">Excluir</el-button>
      </el-header>
      <!-- componente  cabecalho-hospedagem -->
      <el-main style="font-size:10pt; padding:0px 20px;" v-if="hospedagem != null">
        <el-row style="padding:5px;">
          <el-col :sm="24" :md="24" :lg="24">
            <el-row :gutter="10">
              <el-col :span="4">Número da Hospedagem</el-col>
              <el-col :span="4" class="font-weight-bold">{{hospedagem.id}}</el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="4">Data de Entrada</el-col>
              <el-col :span="4" class="font-weight-bold">{{hospedagem.dataEntrada | fmtData}}</el-col>
              <el-col :span="4">Data Prevista de Saída</el-col>
              <el-col :span="4" class="font-weight-bold">{{hospedagem.dataPrevistaSaida | fmtData}}</el-col>
              <el-col :span="4">Data Efetiva de Saída</el-col>
              <el-col :span="4" class="font-weight-bold">{{hospedagem.dataEfetivaSaida | fmtData}}</el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="4">Destinação</el-col>
              <el-col :span="4" class="font-weight-bold">{{hospedagem.destinacaoHospedagem != null ? hospedagem.destinacaoHospedagem.descricao : null}}</el-col>
              <el-col :span="4">Tipo de Utilização</el-col>
              <el-col :span="4" class="font-weight-bold">{{hospedagem.tipoUtilizacao | fmtTipoUtilizacao}}</el-col>
              <el-col :span="4">Situação</el-col>
              <el-col :span="4" class="font-weight-bold">{{hospedagem.status}}</el-col>
            </el-row>

            <el-row :gutter="10" v-if="hospedagem.servicos != null && hospedagem.servicos.length > 0">
              <el-col :span="6">Serviços</el-col>
              <el-col :span="16" class="font-weight-bold">
                <el-tag v-for="(servico, idx) in hospedagem.servicos" :key="idx" type="success" size="medium" class="tags">{{servico.descricao}}</el-tag>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="8">Observações</el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="24">
                <el-input type="textarea" class="font-size-9" rows="3" v-model="hospedagem.observacoes" readonly></el-input>                      
              </el-col>
            </el-row>
          </el-col>
        </el-row>

        <el-row type="flex" :gutter="10" style="padding:5px;">
          <el-col :span="12">

            <div>
              <el-row>
                <el-col>
                  <div class="subtitulo bg-purple">HÓSPEDE(S)</div>
                </el-col>
              </el-row>

              <el-row :gutter="10">
                <el-col :span="24">
                  <el-collapse v-for="(hpd, i) in hospedagem.hospedes" :key="i" v-model="activeName" accordion>
                    <el-collapse-item :title="hpd.pessoa.nome + ' - ' + hpd.tipoHospede.descricao" :name="i" 
                      :class="{'grey-lighten-4' : !hpd.baixado != 'S', 'amber-lighten-4' : hpd.baixado == 'S'}">   
                      <el-row v-if="hpd.baixado == 'S'" class="font-weight-bold">
                        BAIXADO
                      </el-row>
                      <el-row v-if="permitirEditar" :gutter="10">
                        <el-col>
                          <!-- showSelecionarDataBaixa(hpd.id, hospedagem.dataPrevistaSaida) -->
                          <el-button type="primary" size="mini" v-if="hospedagem.dataEfetivaSaida == null && hpd.baixado != 'S'" 
                            @click.native="showSelecionarDataBaixa(hpd.id)">Baixar
                          </el-button>
                          <el-button type="primary" size="mini" v-if="hospedagem.dataEfetivaSaida == null && hpd.baixado != 'S' && hospedagem.tipoUtilizacao == 'T'" 
                            @click.native="showTransferenciaLeito(hpd)">Transferir
                          </el-button>
                        </el-col>
                      </el-row>

                      <el-row :gutter="10">
                        <el-col :span="4">Nascimento</el-col>
                        <el-col :span="20" class="font-weight-bold">{{hpd.pessoa.dataNascimento | fmtData}}</el-col>
                      </el-row>
                      <el-row :gutter="10">
                        <el-col :span="4">Endereço</el-col>
                        <el-col :span="20" class="font-weight-bold">{{hpd.pessoa.endereco != null ? hpd.pessoa.endereco.descricao : ''}}</el-col>
                      </el-row>

                      <div xs12 sm12 md12 v-if="hospedagem.tipoUtilizacao == 'T'">
                        <el-row>
                          <el-col>Histórico de Leitos</el-col>
                        </el-row>

                        <el-row v-for="(leito, leitoIndex) in hpd.leitos" :key="leitoIndex">
                          <el-col>
                          #{{leito.id}} - <span></span>{{leito.dataEntrada | fmtData}} - Quarto {{leito.quartoNumero}} Leito {{leito.leitoNumero}}
                          <span v-if="(hpd.baixado == 'S') && (leitoIndex == hpd.leitos.length-1)" class="font-weight-bold"> - Baixado em {{leito.dataSaida | fmtData}}</span>
                          </el-col>
                        </el-row>
                      </div> 
                    </el-collapse-item>
                  </el-collapse> 
                </el-col>
              </el-row>
            </div>  
          </el-col>

          <!-- componente encaminhador -->
          <el-col :span="12">
            <el-row>
              <el-col>
                <div class="subtitulo bg-purple">ENCAMINHADOR</div>
              </el-col>
            </el-row>

            <frame-encaminhador v-if="encaminhador != null && entidade != null"
              :encaminhador="encaminhador" 
              :entidade="entidade">
            </frame-encaminhador>

          </el-col>
        </el-row>

      </el-main>
    </el-container>

    <dialogo-encerrar-hospedagem 
      :visible="dialogoEncerramentoVisible" 
      :hospedagemId="hospedagemId" 
      @close="onCloseDialogoEncerramento">
    </dialogo-encerrar-hospedagem>

    <dialogo-renovar-hospedagem 
      :visible="dialogoRenovacaoVisible" 
      :hospedagemId="hospedagemId" 
      @close="onCloseDialogoRenovacao">
    </dialogo-renovar-hospedagem>

    <dialogo-baixar-hospede
      :visible="dialogoBaixaVisible" 
      :hospedeId="hospedeId" 
      @close="onCloseDialogoBaixa">
    </dialogo-baixar-hospede>

    <dialogo-excluir-hospedagem
      :visible="dialogoExclusaoVisible" 
      :hospededagemId="hospedagemId" 
      @close="onCloseDialogoExclusao">
    </dialogo-excluir-hospedagem>

    <dialogo-transferencia-leito 
      :visible="dialogoTransferenciaLeitoVisible" 
      :config="configTransferenciaLeito" 
      @selecionar="onTransferirLeito">
    </dialogo-transferencia-leito>    

    <dialogo-acrescentar-hospede
      :visible="dialogoAcrescentarHospedeVisible" 
      :config="configAcrescentarHospede" 
      @selecionar="onAcrescentarHospede">
    </dialogo-acrescentar-hospede>    

  </div>

</template>

<script>

import ListagemErros from "../../components/ListagemErros.vue"
import DialogoEncerrarHospedagem from "./DialogoEncerrarHospedagem.vue"
import DialogoRenovarHospedagem from "./DialogoRenovarHospedagem.vue"
import DialogoBaixarHospede from "./DialogoBaixarHospede.vue"
import DialogoExcluirHospedagem from "./DialogoExcluirHospedagem.vue"
import DialogoTransferenciaLeito from "./DialogoTransferenciaLeito.vue"
import DialogoAcrescentarHospede from "./DialogoAcrescentarHospede.vue"
import FrameEncaminhador from "./FrameEncaminhador.vue"

export default {
  name: 'TelaHospedagem',

  components: {
    ListagemErros,
    DialogoEncerrarHospedagem,
    DialogoRenovarHospedagem,
    DialogoBaixarHospede,
    DialogoExcluirHospedagem,
    DialogoTransferenciaLeito,
    DialogoAcrescentarHospede,
    FrameEncaminhador,
  },

  created(){},

  mounted(){
    this.$store.dispatch('setAcao','Detalhes de Hospedagem')
    this.hospedagemId = 60
    this.getInfo(this.hospedagemId)
  },

  data: () =>({
    state : 'browse',

    dados: [],

    //Talvez seja props
    hospedagemId : null,
    hospedeId : null,

    hospedagem : {},
    entidade : {},
    encaminhador : {},
    hospedeSelecionado : {},

    formRenovacao : {
      dataRenovacao : null
    },

    formBaixa : {
      dataBaixa : null
    },

    configTransferenciaLeito : {
      hospede : null, 
      destinacaoHospedagemId : null,
      dataIni : null, 
      dataFim: null
    },

    activeName: null,
    permitirEditar : true,
    permitirRenovar : true,
    permitirEncerrar : true,
    permitirAcrescentar : true,

    configAcrescentarHospede : null,
    dialogAcrescentarHospede : false,

    dialogoEncerramentoVisible : false,
    dialogoRenovacaoVisible : false,
    dialogoBaixaVisible : false,
    dialogoExclusaoVisible : false,
    dialogoTransferenciaLeitoVisible : false,
    dialogoAcrescentarHospedeVisible : false,

    errors: [],

  }),

  watch: {},

  created(){},

  computed: {},

  methods: {

    getInfo(hospedagemId) {
      var dados = {
        hospedagemId : hospedagemId
      }

      petra.axiosPost("/app/hospedagem/mapa/hospedagem_info", dados, false)
        .then(response => { 
            this.hospedagem = response.data
            this.entidade = (this.hospedagem && this.hospedagem.entidade) ? this.hospedagem.entidade : null
            this.encaminhador = (this.hospedagem && this.hospedagem.encaminhador) ? this.hospedagem.encaminhador : null
            /*
            this.hospedes = this.hospedagem.hospedes
            this.servicos = (this.hospedagem && this.hospedagem.servicos) ? this.hospedagem.servicos : []
            this.tipoHospede = this.hospedagem.hospedes[0].tipoHospede
            this.destinacaoHospedagem = this.hospedagem.destinacaoHospedagem
            */
          }).catch(error => {
            this.errors = petra.tratarErros(error);
          })
    },

    //Encerramento
    showSelecionarDataEncerramento(hospedagemId, dataPrevistaSaida){
      this.dialogoEncerramentoVisible = true
    },

    onCloseDialogoEncerramento(sucesso){
      this.dialogoEncerramentoVisible = false
      if (sucesso){
        this.getInfo(this.hospedagemId)
      }
    },

    //Renovação
    showSelecionarDataRenovacao(hospedagemId, dataPrevistaSaida){
      this.dialogoRenovacaoVisible = true
    },

    onCloseDialogoRenovacao(sucesso){
      this.dialogoRenovacaoVisible = false
      if (sucesso){
        this.getInfo(this.hospedagemId)
      }
    },

    //Exclusão
    showConfirmarExclusao(hospedagemId){
      this.dialogoExclusaoVisible = true
    },

    onCloseDialogoExclusao(sucesso){
      this.dialogoExclusaoVisible = false
      if (sucesso){
        // TODO Sair dessa hospedagem e direcionar para hospedagens
        //this.getInfo(this.hospedagemId)
      }
    },

    // Transferência
    showSelecionarTransferencia(hpd){
      this.hospedeSelecionado = hpd
      this.formTransferencia.dataTransferencia = null
      this.state = 'transferir'
      this.configTransferencia = {
        hospede : hpd,
        destinacaoHospedagemId : this.destinacaoHospedagem.id,
        dataEntrada : this.hospedagem.dataEntrada,
        dataPrevistaSaida : this.hospedagem.dataPrevistaSaida
      }
      //this.$refs.frameSelecaoLeito.openDialog(hpd, this.destinacaoHospedagem.id, this.hospedagem.dataEntrada, this.hospedagem.dataPrevistaSaida)
    },

    handleTransferirHospede(){
      if (this.hospedeSelecionado != null && this.formTransferencia.dataTransferencia != null && this.formTransferencia.acomodacao != null){
        this.transferirHospede(this.hospedeSelecionado.id, this.formTransferencia)
      }
    },

    transferirHospede(hospedeId, data){
      //var selecao = this.$refs.frameSelecaoLeito.getSelecao()
      var dados = {
        hospedeId : hospedeId,
        leitoId : data.acomodacao.leito.id,
        data : data.dataTransferencia
      }

      //console.log("dados para transferencia:",dados);
      petra.axiosPost("/app/hospedagem/mapa/transferir", dados)
        .then(response => {
          this.getInfo(this.hospedagemId)
          petra.showMessageSuccess('Hóspede transferido com sucesso')
          this.state = 'browse'
          this.dialogVisible = false
        }).catch(error => {
          this.errors = petra.tratarErros(error);
          //console.log(this.errors)
          //petra.showMessageError(this.errors)
        })
    },

    //Baixa de Hóspede
    showSelecionarDataBaixa(hospedeId){
      this.hospedeId = hospedeId
      this.dialogoBaixaVisible = true
    },

    onCloseDialogoBaixa(sucesso){
      this.dialogoBaixaVisible = false
      if (sucesso){
        this.getInfo(this.hospedagemId)
      }
    },

    showTransferenciaLeito(hospede) {
      this.hospedeSelecionado = hospede

      this.configTransferenciaLeito = {
        hospede : hospede, 
        destinacaoHospedagemId : this.hospedagem.destinacaoHospedagem.id,
        dataIni : this.hospedagem.dataEntrada, 
        dataFim: this.hospedagem.dataPrevistaSaida        
      }

      this.dialogoTransferenciaLeitoVisible = true
    },

    onTransferirLeito(sucesso){
      this.dialogoTransferenciaLeitoVisible = false

      this.configTransferenciaLeito = {
        hospede : null, 
        destinacaoHospedagemId : null,
        dataIni : null, 
        dataFim: null
      }

      if (sucesso) {
        this.getInfo(this.hospedagemId)
      }

    },

    showSelecionarAcrescentarHospede() {
      this.configAcrescentarHospede = {
        destinacaoHospedagemId : this.hospedagem.destinacaoHospedagem.id,
        dataIni : this.hospedagem.dataEntrada, 
        dataFim: this.hospedagem.dataPrevistaSaida        
      }

      this.dialogoAcrescentarHospedeVisible = true
    },

    onAcrescentarHospede(sucesso){
      this.dialogoAcrescentarHospedeVisible = false

      this.configAcrescentarHospede = {
        destinacaoHospedagemId : null,
        dataIni : null, 
        dataFim: null
      }

      if (sucesso) {
        this.getInfo(this.hospedagemId)
      }

    },

  }
}
</script>

<style scoped>
</style>
