<template>
  <div>
    <v-dialog v-model="dialogVisible" width="800">
      <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          Incluir Pessoa
        </v-card-title>

        <v-card-text>
          <v-layout row wrap>
            <v-flex xs12 sm8 md8>
              <v-text-field label="Nome" ref="edtNome" v-model="form.nome" :error-messages="getErrors('nome')"></v-text-field>
            </v-flex>

            <v-flex xs12 sm4 md4>
              <v-text-field label="Nascimento" v-model="form.dataNascimento" :mask="'##/##/####'" :error-messages="getErrors('dataNascimento')"></v-text-field>
            </v-flex>

            <v-flex xs12 sm6 md6>
              <v-select label="Sexo" v-model="form.sexo" :items="itensSexo" :error-messages="getErrors('sexo')"></v-select>
            </v-flex>
            <v-flex xs12 sm6 md6>
              <v-select label="Estado Civil" v-model="form.estadoCivil" :items="itensEstadoCivil" :error-messages="getErrors('estadoCivil')"></v-select>
            </v-flex>

            <v-flex xs12 sm6 md6>
              <v-text-field label="Telefone" v-model="form.telefone" :error-messages="getErrors('telefone')"></v-text-field>
            </v-flex>
            <v-flex xs12 sm6 md6>
              <v-text-field label="Email" v-model="form.email" :error-messages="getErrors('email')"></v-text-field>
            </v-flex>

            <v-flex xs12 sm6 md6>
              <v-text-field label="CPF" v-model="form.cpf" :mask="'###.###.###-##'" :masked="true" :error-messages="getErrors('cpf')"></v-text-field>
            </v-flex>
            <v-flex xs12 sm6 md6>
              <v-text-field label="RG" v-model="form.rg" :error-messages="getErrors('rg')"></v-text-field>
            </v-flex>

            <v-flex xs12 sm6 md6>
              <v-text-field label="Endereço" v-model="form.endereco.logradouro" :error-messages="getErrors('endereco.logradouro')"></v-text-field>
            </v-flex>
            <v-flex xs12 sm2 md2>
              <v-text-field label="Número" v-model="form.endereco.numero" :error-messages="getErrors('endereco.numero')"></v-text-field>
            </v-flex>
            <v-flex xs12 sm4 md4>
              <v-text-field label="Complemento" v-model="form.endereco.complemento" :error-messages="getErrors('endereco.complemento')"></v-text-field>
            </v-flex>

            <v-flex xs12 sm4 md4>
              <v-text-field label="Bairro" v-model="form.endereco.bairro" :error-messages="getErrors('endereco.bairro')"></v-text-field>
            </v-flex>
            <v-flex xs12 sm2 md2>
              <v-text-field label="CEP" v-model="form.endereco.cep" :mask="'#####-###'" :error-messages="getErrors('endereco.cep')"></v-text-field>
            </v-flex>
            <v-flex xs12 sm4 md4>
              <v-text-field label="Cidade" v-model="form.endereco.cidade" :error-messages="getErrors('endereco.cidade')"></v-text-field>
            </v-flex>
            <v-flex xs12 sm2 md2>
              <v-select label="UF" v-model="form.endereco.uf" :items="itensUF" item-text="value" item-value="value" :error-messages="getErrors('endereco.uf')"></v-select>
            </v-flex>
          </v-layout>

        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions class="grey lighten-4"> 
          <v-spacer></v-spacer>
          <v-btn small dark color="cyan darken-4" @click.native="save">
            Ok
          </v-btn>
          <v-btn small color="secondary" @click.native="close(false)">
            Cancelar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>

import {mask} from "vue-the-mask"

export default {
  name: "PessoaEdit",

  directives: {
    mask
  },

  props: {},

  data: () => ({
    dataNascimento: null,
    dataNascimentoFmt: null,
    form: {
      id : null,
      nome : null,
      dataNascimento : null,
      sexo : null,
      estadoCivil : null,
      cpf : null,
      rg : null,
      telefone : null,
      email : null,
      endereco : {
        logradouro : null,
        numero : 0,
        complemento : null,
        bairro : null,
        cep : null,
        cidade : null,
        uf : null
      }
    },
    menuDataNascimento: false,
    dados: [],
    errors: [],
    dialogVisible: false,
    itensDestinacaoHospedagem: [],
    itensTipoLeito: [],
    itensSituacaoLeito: [],
    itensSexo: [{ text: "Masculino", value: "M" }, { text: "Feminino", value: "F" }],
    itensEstadoCivil : [{ text: "Solteiro(a)", value:"S"},{ text: "Casado(a)", value:"C"},{ text: "Divorciado(a)", value:"D"},{ text: "União Estável", value:"U"},{ text: "Viúvo(a)", value:"V"}],
    itensUF : [
      { value : "AC", text : "AC", texto: "Acre" },
      { value : "AL", text : "AL", texto: "Alagoas"}, 
      { value : "AM", text : "AM", texto: "Amazonas"},
      { value : "AP", text : "AP", texto: "Amapá"}, 
      { value : "BA", text : "BA", texto: "Bahia"}, 
      { value : "CE", text : "CE", texto: "Ceará"}, 
      { value : "DF", text : "DF", texto: "Distrito Federal"}, 
      { value : "ES", text : "ES", texto: "Espírito Santo"},
      { value : "GO", text : "GO", texto: "Goiás"}, 
      { value : "MA", text : "MA", texto: "Maranhão"}, 
      { value : "MG", text : "MG", texto: "Minas Gerais"}, 
      { value : "MS", text : "MS", texto: "Mato Grosso do Sul"},
      { value : "MT", text : "MT", texto: "Mato Grosso"}, 
      { value : "PA", text : "PA", texto: "Pará"}, 
      { value : "PB", text : "PB", texto: "Paraíba"}, 
      { value : "PE", text : "PE", texto: "Pernambuco"},
      { value : "PI", text : "PI", texto: "Piauí"}, 
      { value : "PR", text : "PR", texto: "Paraná"},
      { value : "RJ", text : "RJ", texto: "Rio de Janeiro"},
      { value : "RN", text : "RN", texto: "Rio Grande do Norte"},
      { value : "RO", text : "RO", texto: "Rondônia"}, 
      { value : "RR", text : "RR", texto: "Roraima"},
      { value : "RS", text : "RS", texto: "Rio Grande do Sul"}, 
      { value : "SC", text : "SC", texto: "Santa Catarina"},
      { value : "SE", text : "SE", texto: "Sergipe"}, 
      { value : "SP", text : "SP", texto: "São Paulo"}, 
      { value : "TO", text : "TO", texto: "Tocantins"}]
  }),

  created() {
    this.reset();
  },

  watch : {
    dataNascimento(val) {
      this.form.dataNascimentoFmt = this.formatDate(this.form.dataNascimento)
    },   
  },

  methods: {
    openDialog(form) {
      this.reset();
      this.loadListas();
      if (!form) {
        this.reset();
      } else {
        console.log(form)
        this.form = form;
        this.form.dataNascimento = this.formatDate(this.form.dataNascimento)
      }
      this.dialogVisible = true;
      setTimeout(() => {
        this.$refs.edtNome.focus()
      }, 500)
    },

    loadListas(evt) {
      /*
      this.itensDestinacaoHospedagem = [];
      let uri = petra.base_uri + "/quarto/listas";
      axios.get(uri).then(response => {
        this.itensDestinacaoHospedagem =
          response.data.listaDestinacaoHospedagem;
        this.itensTipoLeito = response.data.listaTipoLeito;
        this.itensSituacaoLeito = response.data.listaSituacaoLeito;
      });
      */
    },

    /*
    dataNascimentoBlur(){
      this.dataNascimento = this.parseDate(this.dataNascimentoFmt)
      this.form.dataNascimento = this.parseDate(this.dataNascimentoFmt)
    },
    */

    formatDate(date) {
      if (!date) return null

      const [year, month, day] = date.split('-')
      return `${day}/${month}/${year}`
    },
    
    parseDate(date) {
      if (!date) return null
      const [day, month, year] = date.split('/')
      var retorno = `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
      return retorno
    },    

    // Converte data no formato DDMMYYYY em YYYY-MM-DD
    newParseDate(st){
      //var st = "26042013";
      var pattern = /(\d{2})(\d{2})(\d{4})/;
      //var dt = new Date(st.replace(pattern,'$3-$2-$1'));
      return st.replace(pattern,'$3-$2-$1');
    },

    newParseDateOld(date){
      var st = "26.04.2013";
      var pattern = /(\d{2})\.(\d{2})\.(\d{4})/;
      var dt = new Date(st.replace(pattern,'$3-$2-$1'));      
    },

    close(value) {
      this.dialogVisible = false;
      this.$emit("close", false);
    },

    save(evt) {
      this.errors = [];
      this.form.dataNascimento = this.newParseDate(this.form.dataNascimento)

      petra.axiosPost("/app/pessoas", this.form)
        .then(response => {
            this.dialogVisible = false
            this.$emit('close',true)
            this.$emit('save',response.data)
        })
        .catch(error => {
          this.errors = petra.tratarErros(error)
        })
    }, 

    reset(evt) {
      this.errors = [];
    },

    getErrors(fieldName) {
      return petra.getErrors(fieldName, this.errors);
    }
  }
};
</script>
<style scoped>
  v-input {
    font-size : 12pt;
  }
</style>