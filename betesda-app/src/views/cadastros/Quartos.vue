<template>
  <div class="divmain">

    <el-container v-if="state == 'browse'">
      <el-header>
        <el-tooltip content="Incluir novo Quarto" placement="bottom" :open-delay="toolTipDelay">
          <el-button type="primary" @click="handleInsert">Incluir</el-button>
        </el-tooltip>
      </el-header>
      <el-main>
        <el-row type="flex" justify="center" align="middle">
          <el-col :sm="24" :md="24" :lg="24">

            <el-tabs type="border-card">
              <el-tab-pane v-for="(quarto, index) in dados" :key="index" :label="'Quarto ' + quarto.numero">

                <el-row type="flex" style="margin-bottom:10px">
                  <el-col>
                  <el-button type="primary" size="mini" @click="handleInsert">Novo Leito</el-button>
                  <el-button type="primary" size="mini" @click="handleInsert">Alterar Quarto</el-button>
                  <el-button type="danger" size="mini" @click="handleInsert">Excluir Quarto</el-button>
                  </el-col>
                </el-row>

                <el-row type="flex" justify="center">
                  <el-col><span class="subtitle">Destinações de Hospedagem:</span><el-tag v-for="(dest, idx) in quarto.destinacoes" :key="idx" type="success" size="medium" class="destinacoes">{{dest.descricao}}</el-tag></el-col>
                </el-row>

                <div class="flex-container wrap">
                  <el-card class="flex-item" v-for="(leito, i)  in dados[index].leitos" :key="i" shadow="always" style="width:400px;">

                    <div slot="header" class="clearfix">
                      <span class="numero_leito">{{leito.numero}}</span>

                      <!--

                  <el-tooltip content="Editar" placement="bottom" :open-delay="toolTipDelay">
                    <el-button type="primary" plain size="mini" circle @click="handleEdit(scope.row)"><i class="fas fa-pencil-alt"></i></el-button>  
                  </el-tooltip>
                  <el-tooltip content="Excluir" placement="bottom" :open-delay="toolTipDelay">
                    <el-button type="danger"  plain size="mini" circle @click="handleDelete(scope.row)"><i class="fas fa-trash"></i></el-button>  
                  </el-tooltip>


                      -->  


                      <el-tooltip content="Excluir Leito" placement="bottom" :open-delay="toolTipDelay">
                          <el-button @click="handleDeleteLeito(leito.id)" style="float: right; margin-left:10px" plain size="mini" circle type="danger"><i class="fas fa-trash"></i></el-button>
                      </el-tooltip>
                      <el-tooltip content="Editar Leito" placement="bottom" :open-delay="toolTipDelay">
                          <el-button @click="handleEditLeito(leito.id)" style="float: right;" plain size="mini" circle type="primary"><i class="fas fa-pencil-alt"></i></el-button>
                      </el-tooltip>
                    </div> 
                    <div style="font-size:0.8em">{{leito.tipoLeito.descricao}} - {{leito.tipoLeito.quantidadeCamas}} cama(s)</div>                      
                  </el-card>
                </div>
                
              </el-tab-pane>
            </el-tabs>            

<!--

        <v-tabs fixed-tabs v-model="active" v-on:input="inputTabEvent" slider-color="cyan darken-2">
          <v-tab v-for="(quarto, index)  in dados" :key="index" ripple>
            Quarto {{ quarto.numero }}
          </v-tab>

          <v-tab-item v-for="(quarto, index) in dados" :key="index" ripple>
            <v-layout row wrap class="subheading pa-2">
              <v-flex xs12 class="subheading ml-2">
                  Destinações de Hospedagem:
                  <v-chip color="amber lighten-2" v-for="(destinacaoHospedagem, idx) in quarto.destinacoes" :key="idx">{{destinacaoHospedagem.descricao}}</v-chip>
              </v-flex>

              <v-flex xs12>
                <v-btn small color="cyan darken-4" class="white--text" @click="incluirLeito(quarto)">Novo Leito<v-icon right dark>add</v-icon></v-btn>
                <v-btn small color="cyan darken-4" class="white--text" @click="editItem(quarto)">Alterar Quarto<v-icon right dark>edit</v-icon></v-btn>
                <v-btn small color="cyan darken-4" class="white--text" @click="deleteItem(quarto)">Excluir Quarto<v-icon right dark>delete</v-icon></v-btn>
              </v-flex>

              <v-container fluid grid-list-lg>
                <v-layout row wrap>
                  <v-flex xs3 v-for="(leito, i)  in dados[index].leitos" :key="i" >
                    <v-card :class="classeSituacaoLeito(leito.id)">
                      <h2 class="headline ml-2 mt-0">{{leito.numero}}</h2>  
                      <v-card-text class="body-1 mb-0">
                        <div>{{leito.tipoLeito.descricao}} - {{leito.tipoLeito.quantidadeCamas}} cama(s)</div>
                      </v-card-text>

                      <v-divider light></v-divider>
                      <v-card-actions class="grey lighten-4">
                        <v-spacer></v-spacer>

                        <v-tooltip bottom>
                          <v-btn icon slot="activator" @click="editLeito(quarto, leito)">
                            <v-icon>edit</v-icon>
                          </v-btn>
                          <span>Editar Leito</span>
                        </v-tooltip>
                        

                        <v-tooltip bottom>
                          <v-btn icon slot="activator" @click="deleteLeito(quarto, leito)">
                            <v-icon>delete</v-icon>
                          </v-btn>
                          <span>Excluir Leito</span>
                        </v-tooltip>

                        <v-tooltip bottom v-if="isLeitoOcupado(leito.id)" >
                          <v-btn icon slot="activator" @click="visualizarHospedagemNoLeito(leito.id)" >
                            <v-icon color="red darken-4">fa-users</v-icon>
                          </v-btn>
                          <span>Visualizar Hospedagem atual</span>
                        </v-tooltip>

                      </v-card-actions>
                    </v-card>
                  </v-flex>
                </v-layout>
              </v-container>

            </v-layout>
          </v-tab-item>
        </v-tabs>
-->
          </el-col>
        </el-row>
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
          <div>{{state=='edit' ? 'Editando Destinaçao de Hospedagem' : 'Incluindo Destinaçao de Hospedagem'}}</div>
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

  name: 'Quartos',

  created(){
  },

  mounted(){
    this.$store.dispatch('setAcao','Quartos')
    this.doGetAll()
  },

  data: () =>({
    dados: [],

    erros: [],

    state : 'browse',

    dialogExclusaoVisible : false,

    idToDelete : null,
    textToDelete : null,

    form : {
      id : null,
      descricao : null,
      disponivel : null
    },

    rules: {
      descricao: [
        { required: true, message: "Preencha a descrição",trigger: "blur"},
        { min: 3, max: 32, message: "Descrição deve ter de 3 a 32 caracteres",trigger: "blur"}
      ],
    },

    toolTipDelay: 500

  }),

  methods: {

    resetData(){
      this.form = {
        id : null,
        descricao : null,
        disponivel : null
      }
      this.erros = []
    },

    setDefaultData(){
    },

    getErro(campo){
      var retorno =  _.find(this.erros,{fieldName : campo})
      if (retorno){
        return retorno.errorMessage
      }
      return null
    },

    handleEditLeito(id){
      console.log("handleEditLeito ",id)
    },

    handleDeleteLeito(id){
      console.log("handleDeleteLeito ",id)
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
      this.textToDelete = `Deseja realmente excluir a Destinação de Hospedagem "${row.descricao}"?`
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
      petra.axiosGet("/app/quarto").then(
        response => {
          console.log(response.data)
          this.dados = response.data
        })
    },

    doGetById(id) {
      petra.axiosGet(`/app/destinacao_hospedagem/${id}`).then(
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
      petra.axiosDelete("/app/destinacao_hospedagem/"+this.idToDelete)
        .then(response => {
          petra.showMessageSuccess('Destinação de Hospedagem excluída com sucesso')
          this.doGetAll()
        })
        .catch(error => {
          petra.tratarErros(error)
        })
    },

    doSave(evt) {
      this.errors = [];

      petra.axiosPost("/app/destinacao_hospedagem/", this.form, false)
        .then(response => {
          petra.showMessageSuccess('Destinação de Hospedagem gravada com sucesso')
          this.state="browse"
          this.doGetAll()
        })
        .catch(error => {
          this.erros = petra.tratarErros(error)
        })
    },

  }
}
</script>

<style scoped>
  .divmain{
    color: #546E7A;
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

  .destinacoes {
    margin-right: 5px;
  }

  .subtitle {
    font-size: .8em;
    margin-right: 10px;
  }

  .numero_leito{
    font-size: 1.2em;
  }

.flex-container {
  padding: 0;
  margin: 0;
  list-style: none;
  /*border: 1px solid silver;*/
  box-orient: horizontal;
  /*-ms-box-orient: horizontal;*/
  display: flex;
  justify-content: center;
}

.wrap    { 
  flex-wrap: wrap;
}  

.wrap li {
  background: gold;
}

.flex-item {
  background: #FFF8E1;
  color: #455A64;
  padding: 2px;
  width: 300px;
  /*height: 100px;*/
  margin: 10px;
  
  /*
  line-height: 100px;
  color: white;
  font-weight: bold;
  font-size: 2em;
  text-align: center;
  */
}

</style>
