<template>
  <div>

    <el-container v-if="state == 'browse'">
      <el-header>
        <el-tooltip content="Incluir nova Situação de Leito" placement="bottom" :open-delay="toolTipDelay">
          <el-button type="primary" @click="handleInsert">Incluir</el-button>
        </el-tooltip>
      </el-header>
      <el-main>
        <el-row type="flex" justify="center" align="middle">
          <el-col :sm="24" :md="24" :lg="24">
            <el-table
              :data="dados"
              stripe 
              style="width: 100%"
              border size="small"
              :default-sort="{prop: 'descricao', order: 'ascending'}" 
              height="400">
              <el-table-column fixed header-align="left" align="right" prop="id" label="Código" width="100"></el-table-column>
              <el-table-column fixed prop="nome" sortable label="Nome" width="250"></el-table-column>
              <el-table-column prop="dataNascimento" :formatter="fmtDate" label="Nascimento" header-align="left" width="120" sortable ></el-table-column>
              <el-table-column prop="endereco.descricao" sortable label="Endereço" class-name="wordwrap" width="350"></el-table-column>
              <el-table-column prop="naturalidadeCidade" sortable label="Naturalidade" width="120"></el-table-column>
              <el-table-column prop="naturalidadeUf" sortable label="UF" ></el-table-column>
              <el-table-column label="Ações" fixed="right" width="120">
                <template slot-scope="scope">
                  <el-tooltip content="Editar" placement="bottom" :open-delay="toolTipDelay">
                    <el-button type="primary" plain size="mini" circle @click="handleEdit(scope.row)"><i class="fas fa-pencil-alt"></i></el-button>  
                  </el-tooltip>
                  <el-tooltip content="Excluir" placement="bottom" :open-delay="toolTipDelay">
                    <el-button type="danger"  plain size="mini" circle @click="handleDelete(scope.row)"><i class="fas fa-trash"></i></el-button>  
                  </el-tooltip>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-main>
    </el-container>

    <el-container v-if="state != 'browse'">
      <el-header>
        <el-row type="flex">  
          <el-tooltip content="Gravar alterações" placement="bottom" :open-delay="toolTipDelay">
            <div style="margin-right:10px;"><el-button type="primary" @click="state = browse">Gravar</el-button></div>
          </el-tooltip>
          <el-tooltip content="Cancelar alterações" placement="bottom" :open-delay="toolTipDelay">
            <div style="margin-right:10px;"><el-button @click="state = browse">Cancelar</el-button></div>
          </el-tooltip>
          <div>Incluindo/Alterando Pessoa</div>
        </el-row>
      </el-header>
      <el-main>
        
        <el-form :model="form" :rules="rules" ref="form" label-position="top" size="small" label-width="140px">

            <el-row :gutter="10">                      
              <el-col :span="16">
                  <el-form-item label="Nome" prop="fixme" :error="getErro('fixme')">
                    <el-input ></el-input>
                  </el-form-item>
              </el-col>
              <el-col :span="8">
                  <el-form-item label="Nascimento" prop="fixme" :error="getErro('fixme')">
                    <el-input ></el-input>
                  </el-form-item>
              </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-tabs type="border-card">
                <el-tab-pane label="Dados Pessoais">
                  
                  <el-row :gutter="10">                      
                    <el-col :span="6">
                        <el-form-item label="Sexo" prop="fixme" :error="getErro('fixme')">
                          <el-input ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="Estado Civil" prop="fixme" :error="getErro('fixme')">
                          <el-input ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="Profissão" prop="fixme" :error="getErro('fixme')">
                          <el-input ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="10">                      
                  <el-col :span="12">
                      <el-form-item label="Naturalidade" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="4">
                      <el-form-item label="Nat.UF" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="Nacionalidade" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="10">                      
                  <el-col :span="8">
                      <el-form-item label="CPF" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="RG" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="Cartão do SUS" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                </el-row>
              </el-tab-pane>
              <el-tab-pane label="Endereço e Telefone">

                <el-row :gutter="10">
                  <el-col :span="12">
                      <el-form-item label="Endereço" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="4">
                      <el-form-item label="Número" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="Complemento" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="10">
                  <el-col :span="8">
                      <el-form-item label="Bairro" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="4">
                      <el-form-item label="CEP" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="Cidade" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="4">
                      <el-form-item label="UF" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="10">
                  <el-col :span="6">
                      <el-form-item label="Telefone" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="6">
                      <el-form-item label="Telefone2" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="12">
                      <el-form-item label="Email" prop="fixme" :error="getErro('fixme')">
                        <el-input ></el-input>
                      </el-form-item>
                  </el-col>
                </el-row>

              </el-tab-pane>
              <el-tab-pane label="Observações">
                <el-row :gutter="10">
                  <el-col :span="24">
                      <el-form-item label="Telefone" prop="fixme" :error="getErro('fixme')">
                        <el-input type="textarea" rows="10"></el-input>
                      </el-form-item>
                  </el-col>
                </el-row>

              </el-tab-pane>
            </el-tabs>
          </el-col>
        </el-row>



        </el-form>

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

  name: 'Pessoas',

  created(){
  },

  mounted(){
    this.$store.dispatch('setAcao','Pessoas')
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
      this.form.disponivel = 'S'
    },

    fmtSimNao(row, col){
      return (row.disponivel == 'S' ? 'Sim' : 'Não')
    },

    fmtDate(row, col){
      return petraDateTime.formatDate(row.dataNascimento,null)
    },

    formatDate(data,formato){
      return petraDateTime.formatDate(data,formato)
    },

    getErro(campo){
      var retorno =  _.find(this.erros,{fieldName : campo})
      if (retorno){
        return retorno.errorMessage
      }
      return null
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
        //setTimeout(() => this.$refs.edtdescricao.focus(), 500)
      })      
    },
    
    handleSave(){
      this.doSave()
    },
    
    handleDelete(row){
      this.textToDelete = `Deseja realmente excluir "${row.nome}"?`
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
      petra.axiosGet("/app/pessoas").then(
        response => {
          this.dados = response.data
        })
    },

    doGetById(id) {
      petra.axiosGet(`/app/pessoas/${id}`).then(
        response => {
          this.form = response.data
          this.state = "edit"
          this.$nextTick(() => {
            setTimeout(() => {
              //this.$refs.edtdescricao.focus()
            }, 500)
          })      
        })
    },

    doDelete(evt) {
      petra.axiosDelete("/app/pessoas/"+this.idToDelete)
        .then(response => {
          petra.showMessageSuccess('Pessoa excluída com sucesso')
          this.doGetAll()
        })
        .catch(error => {
          petra.tratarErros(error)
        })
    },

    doSave(evt) {
      this.errors = [];

      petra.axiosPost("/app/pessoas/", this.form, false)
        .then(response => {
          petra.showMessageSuccess('Pessoa gravada com sucesso')
          this.state="browse"
          this.doGetAll()
        })
        .catch(error => {
          this.erros = petra.tratarErros(error)
        })
    },

    tableCellClassName({row, column, rowIndex, columnIndex}) {
      // row = mostra o registro do momento. 
      // column = mostra os detalhes da coluna. Exemplo: property (prop)
      // rowIndex e columnIndex os indices absolutos. A primeira linha de registros é 0
        if ((rowIndex === 2) && (columnIndex === 2)) {
          //console.log(row)
          return 'vermelho';
        } else if ((rowIndex === 1) && (columnIndex === 1)){
          return 'azul';
        }
        return 'wordwrap';
    },    

  }
}
</script>

<style scoped>
  .el-header {
    line-height: 60px;
  }

  .el-main{
    padding-top:0px;
  }

  .el-form-item{
    padding-bottom: 0;
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
