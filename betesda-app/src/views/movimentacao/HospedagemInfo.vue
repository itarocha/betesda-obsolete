<template>
  <div>
    <el-dialog title="Informações de Hospedagem" :visible.sync="dialogVisible" width="750px">
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
          <keep-alive>
            <frame-hospedagem ref="frmHospedagem" :config="configHospedagem" @encerrada="onEncerrada" @renovada="onRenovada" @excluida="onExcluida" @refresh="onRefresh"></frame-hospedagem>
          </keep-alive>
        </el-col>
      </el-row>  

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="deleteConfirm">Excluir</el-button>
        <el-button type="primary" @click="showSelecionarDataRenovacao" v-if="permitirRenovar">Renovar</el-button>
        <el-button type="primary" @click="showSelecionarDataEncerramento" v-if="permitirEncerrar">Encerrar</el-button>
        <el-button type="primary" @click="close(false)" v-if="permitirEncerrar">Fechar</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import FrameHospedagem from './FrameHospedagem.vue'

export default {
  name: 'HospedagemInfo',
  
  components: {
    FrameHospedagem,
  },

  computed: {
    codigo(){
      return this.hospedagemId
    },
    configHospedagem() {
      var retorno =  { hospedagemId : this.hospedagemId, permitirEditar : true}
      console.log("passando ", retorno)
      return retorno
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
    errors:[],
    dialogVisible : false,

    permitirRenovar : false,
    permitirEncerrar : true,

  }),

  created(){
    this.reset()
  },

  methods: {
    openDialog(hospedagemId){
      this.reset()
      this.hospedagemId = hospedagemId
      this.dialogVisible = true

      //console.log(this.$refs.frmHospedagem)
      //this.$refs.frmHospedagem.open(hospedagemId, true)

    },

    onRefresh(hospedagem){
      this.permitirRenovar = hospedagem.dataEfetivaSaida == null
      this.permitirEncerrar = hospedagem.dataEfetivaSaida == null
    },

    showSelecionarDataEncerramento(hospedagemId, dataPrevistaSaida){
      this.$refs.frmHospedagem.showSelecionarDataEncerramento()
    },

    onEncerrada(){
      petra.showMessageSuccess('Hospedagem encerrada com sucesso')
      this.$emit('close',true)
      this.dialogVisible = false
    },

    // Renovação
    showSelecionarDataRenovacao(){
      this.$refs.frmHospedagem.showSelecionarDataRenovacao();
    },

    onRenovada(){
      this.$emit('close',true)
      this.dialogVisible = false
    },

    close(value){
      this.dialogVisible = false
      this.$emit('close',value)
    },

    deleteConfirm() {
      this.$refs.frmHospedagem.deleteConfirm()
    },

    onExcluida(evt) {
      petra.showMessageSuccess('Hospedagem excluída com sucesso')
      this.$emit('close',true)
      this.dialogVisible = false
    },

    save(evt) {
      this.$emit('close',true)          
    },

    reset(evt){
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