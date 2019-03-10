<template>
  <div>
    <el-dialog title="Informações de Hospedagem" :visible.sync="dialogVisible" width="750px">
      <listagem-erros :errors="errors"></listagem-erros>
      <!--
      <el-row type="flex">
        <el-col :span="18">
          <h4>{{nomeHospede}}</h4>
        </el-col>
        <el-col :span="6">
          <h4 v-if="acomodacao != null">Seleção: Quarto {{acomodacao.quarto.numero}} Leito {{acomodacao.leito.numero}}</h4>
        </el-col>
      </el-row>
      -->
      <el-row type="flex">
        <el-col :sm="24" :md="24" :lg="24">
          <!-- @encerrada="onEncerrada" @renovada="onRenovada" @excluida="onExcluida" @refresh="onRefresh" -->
            <frame-hospedagem ref="frmHospedagem" :config="configHospedagem" @refresh="onRefresh"></frame-hospedagem>
        </el-col>
      </el-row>  

      <span slot="footer" class="dialog-footer">
        <el-button type="danger" size="small" @click="deleteConfirm">Excluir</el-button>
        <el-button type="primary" size="small" @click="showSelecionarDataRenovacao" v-if="permitirRenovar">Renovar</el-button>
        <el-button type="primary" size="small" @click="showSelecionarDataEncerramento" v-if="permitirEncerrar">Encerrar</el-button>
        <el-button type="primary" size="small" @click="close(false)">Fechar</el-button>
      </span>
    </el-dialog>

    <el-dialog title="Confirmação" :visible.sync="dialogDeleteHospedagemVisible" width="600px">
      <span>Deseja realmente excluir esta Hospedagem?</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleDeleteHospedagem(false)">Cancelar</el-button>
        <el-button type="primary" @click="handleDeleteHospedagem(true)">Sim</el-button>
      </span>
    </el-dialog>

    <!-- Dialogo data de encerramento -->
    <el-dialog title="Selecionar Data de Encerramento" :visible.sync="dialogSelecionarDataEncerramento" width="500px">
      <el-form :model="formEncerramento" label-position="left" label-width="140px;">
        <el-row type="flex">
          <el-col>
            <el-form-item label="Data de Encerramento">
              <el-date-picker type="date" v-model="formEncerramento.dataEncerramento" format="dd/MM/yyyy" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker>    
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="handleSelecionarDataEncerramento(false)">Cancelar</el-button>
        <el-button type="primary" @click="handleSelecionarDataEncerramento(true)" :disabled="formEncerramento.dataEncerramento == null">Confirmar</el-button>
      </span>
    </el-dialog>

    <!-- Dialogo data de renovação -->
    <el-dialog title="Selecionar Data de Renovação" :visible.sync="dialogSelecionarDataRenovacao" width="500px">
      <el-form :model="formRenovacao" label-position="left" label-width="140px;">
        <el-row type="flex">
          <el-col>
            <el-form-item label="Data de Renovação">
              <el-date-picker type="date" v-model="formRenovacao.dataRenovacao" format="dd/MM/yyyy" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker>    
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="handleSelecionarDataRenovacao(false)">Cancelar</el-button>
        <el-button type="primary" @click="handleSelecionarDataRenovacao(true)" :disabled="formRenovacao.dataRenovacao == null">Confirmar</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>

import FrameHospedagem from './FrameHospedagem.vue'
import ListagemErros from "../../components/ListagemErros.vue"

export default {
  name: 'HospedagemInfo',
  
  components: {
    FrameHospedagem,
    ListagemErros
  },

  computed: {
    configHospedagem() {
      var retorno =  { hospedagemId : this.hospedagemId, permitirEditar : true}
      //console.log("passando ", retorno)
      return retorno
    },
    // config é composto de hospedagemId, permitirEditar
    getErros(){
      return this.errors
    }
  },

  data: () =>({
    descricaoItemExclusao : "???",

    hospedagemId: 0,
    tipoHospede:{},
    destinacaoHospedagem:{},
    hospede: {},
    hospedes: [],
    servicos: [],
    hospedagem:{},
    entidade:{},
    encaminhador:{},
    dados: [],
    errors:[{field:'nome', errorMessage:'Nome deve ser preenchido'}, {field:'idade', errorMessage:'Idade deve ser superior a 18'}, ],
    dialogVisible : false,

    permitirRenovar : false,
    permitirEncerrar : true,

    formEncerramento : {
      dataEncerramento : null
    },

    formRenovacao : {
      dataRenovacao : null
    },

    dialogDeleteHospedagemVisible : false,
    dialogSelecionarDataEncerramento : false,
    dialogSelecionarDataRenovacao : false,

  }),

  created(){
    this.reset()
  },

  methods: {
    openDialog(hospedagemId){
      this.reset()
      this.hospedagemId = hospedagemId
      this.dialogVisible = true
    },

    onRefresh(hospedagem){
      this.permitirRenovar = hospedagem.dataEfetivaSaida == null
      this.permitirEncerrar = hospedagem.dataEfetivaSaida == null
    },

    showSelecionarDataEncerramento(hospedagemId, dataPrevistaSaida){
      this.dialogSelecionarDataEncerramento = true
    },

    // Renovação
    showSelecionarDataRenovacao(){
      this.dialogSelecionarDataRenovacao = true
    },

    handleSelecionarDataRenovacao(ok){
      if (ok && this.formRenovacao.dataRenovacao != null){
        this.doRenovarHospedagem(this.hospedagemId, this.formRenovacao.dataRenovacao)
      }
      this.dialogSelecionarDataRenovacao = false
    },

    doRenovarHospedagem(hospedagemId, data) {
      var dados = {
        hospedagemId : hospedagemId,
        data : data
      }
      
      petra.axiosPost("/app/hospedagem/mapa/renovacao", dados, false)
        .then(response => {
          this.$emit('close',true)
          this.hospedagemId = 0
          this.dialogVisible = false

          petra.showMessageSuccess('Hospedagem renovada com sucesso')
        }).catch(error => {
          this.errors = petra.tratarErros(error);
        })
    },

    close(value){
      this.dialogVisible = false
      this.$emit('close',value)
      this.hospedagemId = 0
    },

    deleteConfirm() {
      //this.$refs.frmHospedagem.deleteConfirm()
      this.dialogDeleteHospedagemVisible = true
    },

    handleDeleteHospedagem(confirm) {
      if (confirm) {
        this.doDeleteHospedagem()
      }
      this.dialogDeleteHospedagemVisible = false
    },

    doDeleteHospedagem(){
      petra.axiosDelete("/app/hospedagem/"+this.hospedagemId, false)
        .then(response => {
          petra.showMessageSuccess('Hospedagem excluída com sucesso')
          this.$emit('close',true)
          this.hospedagemId = 0

          this.dialogVisible = false
        })
        .catch(error => {
          this.errors = petra.tratarErros(error);
        })
    },

    handleSelecionarDataEncerramento(ok){
      if (ok && this.formEncerramento.dataEncerramento != null){
        this.doEncerrarHospedagem(this.hospedagemId, this.formEncerramento.dataEncerramento)
      }
      this.dialogSelecionarDataEncerramento = false
    },

    doEncerrarHospedagem(hospedagemId, data) {
      var dados = {
        hospedagemId : hospedagemId,
        data : data
      }

      petra.axiosPost("/app/hospedagem/mapa/encerramento", dados, false)
        .then(response => {
          this.$emit('close',true)
          this.hospedagemId = 0

          this.dialogVisible = false
          petra.showMessageSuccess('Hospedagem encerrada com sucesso')
        }).catch(error => {
          this.errors = petra.tratarErros(error);
        })
    },

    save(evt) {
      this.$emit('close',true)   
      this.hospedagemId = 0       
    },

    reset(){
      this.form = {
        id : null,
        descricao : null
      },
      this.errors = []
    },

    getErrors(fieldName){
      return petra.getErrors(fieldName, this.errors)
    }

  }
}
</script>