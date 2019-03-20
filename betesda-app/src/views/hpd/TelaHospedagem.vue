<template>
  <div>
    <el-container v-if="state == 'browse'">
      <el-header>
          <el-button type="primary">Encerrar</el-button>
          <el-button type="primary">Renovar</el-button>
          <el-button type="primary">Acrescentar Hóspede</el-button>
          <el-button type="danger">Excluir</el-button>
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
              <el-col :span="4" class="font-weight-bold">{{formatDate(hospedagem.dataEntrada)}}</el-col>
              <el-col :span="4">Data Prevista de Saída</el-col>
              <el-col :span="4" class="font-weight-bold">{{formatDate(hospedagem.dataPrevistaSaida)}}</el-col>
              <el-col :span="4">Data Efetiva de Saída</el-col>
              <el-col :span="4" class="font-weight-bold">{{formatDate(hospedagem.dataEfetivaSaida)}}</el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="4">Destinação</el-col>
              <el-col :span="4" class="font-weight-bold">{{hospedagem.destinacaoHospedagem != null ? hospedagem.destinacaoHospedagem.descricao : null}}</el-col>
              <el-col :span="4">Tipo de Utilização</el-col>
              <el-col :span="4" class="font-weight-bold">{{tipoUtilizacao(hospedagem.tipoUtilizacao)}}</el-col>
              <el-col :span="4">Situação</el-col>
              <el-col :span="4" class="font-weight-bold">{{hospedagem.status}}</el-col>
            </el-row>
            <el-row :gutter="10">
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
                      :class="{'grey-lighten-4' : !isBaixado(hpd), 'amber-lighten-4' : isBaixado(hpd)}">   
                      <el-row v-if="hpd.baixado == 'S'" class="font-weight-bold">
                        BAIXADO
                      </el-row>
                      <el-row v-if="permitirEditar" :gutter="10">
                        <el-col>
                          <!-- showSelecionarDataBaixa(hpd.id, hospedagem.dataPrevistaSaida) -->
                          <el-button type="primary" size="mini" v-if="hospedagem.dataEfetivaSaida == null && hpd.baixado != 'S'" 
                            @click.native="handleSelecionarDataBaixa(hpd)">Baixar
                          </el-button>
                          <el-button type="primary" size="mini" v-if="hospedagem.dataEfetivaSaida == null && hpd.baixado != 'S' && hospedagem.tipoUtilizacao == 'T'" 
                            @click.native="handleSelecionarTransferencia(hpd)">Transferir
                          </el-button>
                        </el-col>
                      </el-row>

                      <el-row :gutter="10">
                        <el-col :span="4">Nascimento</el-col>
                        <el-col :span="20" class="font-weight-bold">{{formatDate(hpd.pessoa.dataNascimento)}}</el-col>
                      </el-row>
                      <el-row :gutter="10">
                        <el-col :span="4">Endereço</el-col>
                        <el-col :span="20" class="font-weight-bold">{{hpd.pessoa.endereco != null ? hpd.pessoa.endereco.descricao : ''}}</el-col>
                      </el-row>

                      <div xs12 sm12 md12 v-if="hospedagem.tipoUtilizacao == 'T'">
                        <el-row>
                          <el-col>Histórico de Leitos</el-col>
                        </el-row>

                        <el-row v-for="(leito, leitoIndex) in hpd.leitos" :key="leitoIndex" class="body-1 pa-1 pl-3">
                            <el-col>
                            #{{leito.id}} - {{formatDate(leito.dataEntrada)}} - Quarto {{leito.quartoNumero}} Leito {{leito.leitoNumero}}
                            <span v-if="(hpd.baixado == 'S') && (leitoIndex == hpd.leitos.length-1)" class="font-weight-bold"> - Baixado em {{formatDate(leito.dataSaida)}}</span>
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

            <div v-if="encaminhador != null && entidade != null">
              <el-row :gutter="5" class="font-size-10">
                <el-col :span="16">Encaminhador</el-col>
                <el-col :span="8">Cargo</el-col>
              </el-row>

              <el-row :gutter="5" class="font-weight-bold">
                <el-col :span="16" >{{encaminhador.nome}}</el-col>
                <el-col :span="8">{{encaminhador.cargo}}</el-col>
              </el-row>

              <el-row :gutter="5" class="font-size-10">
                <el-col :span="8">Telefone</el-col>
                <el-col :span="16">Email</el-col>
              </el-row>

              <el-row :gutter="5" class="font-weight-bold">
                <el-col :span="8">{{encaminhador.telefone}}</el-col>
                <el-col :span="16">{{encaminhador.email}}</el-col>
              </el-row>

              <el-row :gutter="5" class="font-size-10">
                <el-col :span="16">Entidade</el-col>
                <el-col :span="8">CNPJ</el-col>
              </el-row>

              <el-row :gutter="5" class="font-weight-bold">
                <el-col :span="16">{{entidade.nome}}</el-col>
                <el-col :span="8">{{entidade.cnpj}}</el-col>
              </el-row>

              <el-row :gutter="5" class="font-size-10">
                <el-col :span="8">Telefone</el-col>
                <el-col :span="16">Email</el-col>
              </el-row>

              <el-row :gutter="5" class="font-weight-bold">
                <el-col :span="8">{{entidade.telefone}}</el-col>
                <el-col :span="16">{{entidade.email}}</el-col>
              </el-row>

              <el-row :gutter="5" class="font-size-10">
                <el-col :span="12">Endereço</el-col>
                <el-col :span="4">Número</el-col>
                <el-col :span="8">Complemento</el-col>
              </el-row>

              <el-row :gutter="5"  v-if="entidade != null && entidade.endereco != null" class="font-weight-bold">
                <el-col :span="12">{{entidade.endereco.logradouro}}</el-col>
                <el-col :span="4">{{entidade.endereco.numero}}</el-col>
                <el-col :span="8">{{entidade.endereco.complemento}}</el-col>
              </el-row>

              <el-row :gutter="5" class="font-size-10">
                <el-col :span="8">Bairro</el-col>
                <el-col :span="4">CEP</el-col>
                <el-col :span="8">Cidade</el-col>
                <el-col :span="4">UF</el-col>
              </el-row>
              <el-row :gutter="5" v-if="entidade != null && entidade.endereco != null" class="font-weight-bold">
                <el-col :span="8">{{entidade.endereco.bairro}}</el-col>
                <el-col :span="4">{{entidade.endereco.cep}}</el-col>
                <el-col :span="8">{{entidade.endereco.cidade}}</el-col>
                <el-col :span="4">{{entidade.endereco.uf}}</el-col>
              </el-row>
            </div>    
          </el-col>
        </el-row>

      </el-main>
    </el-container>

  </div>

</template>

<script>
//  import HospedagemInfo from "./HospedagemInfo.vue"  



export default {
  name: 'TelaHospedagem',

  components: {},

  created(){},

  mounted(){
    this.$store.dispatch('setAcao','Detalhes de Hospedagem')
    this.getInfo(60)
  },

  data: () =>({
    state : 'browse',

    dados: [],

    hospedagem : {},
    entidade : {},
    encaminhador : {},

    activeName: null,
    permitirEditar : true,

    erros: [],

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

    formatDate(data, formato){
      return petraDateTime.formatDate(data) || '---'
    },
   
    tipoUtilizacao(tipo){
      return tipo == "T" ? "Total" : "Parcial"
    },

    isBaixado(hospede){
      return hospede.baixado == 'S'
    },


  }
}
</script>

<style scoped>
</style>
