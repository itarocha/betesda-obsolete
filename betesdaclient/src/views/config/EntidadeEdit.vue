<template>
  <div>
    <v-dialog v-model="dialogVisible" width="800">
      <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          Incluir Entidade
        </v-card-title>

        <v-card-text>
          <v-layout row wrap>
            <v-flex xs12 sm8 md8>
              <v-text-field label="Nome" ref="edtNome" v-model="form.nome" :error-messages="getErrors('nome')"></v-text-field>
            </v-flex>
            <v-flex xs12 sm4 md4>
              <v-text-field label="CNPJ" v-model="form.cnpj" :mask="'##.###.###/####-##'" :masked="true" :error-messages="getErrors('cnpj')"></v-text-field>
            </v-flex>

            <v-flex xs12 sm6 md6>
              <v-text-field label="Telefone" v-model="form.telefone" :error-messages="getErrors('telefone')"></v-text-field>
            </v-flex>
            <v-flex xs12 sm6 md6>
              <v-text-field label="Email" v-model="form.email" :error-messages="getErrors('email')"></v-text-field>
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
      cnpj : null,
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
      }
      this.dialogVisible = true;
      setTimeout(() => {
        this.$refs.edtNome.focus()
      }, 500)
    },

    loadListas(evt) {
    },

    close(value) {
      this.dialogVisible = false;
      this.$emit("close", false);
    },

    save(evt) {
      this.errors = [];

      let uri = petra.base_uri+"/app/entidades";
      axios.post(uri, this.form)
          .then(response => { 
            this.dialogVisible = false
            this.$emit('close',true)
            this.$emit('save',response.data)
          }).catch(error => {
            this.errors = petra.tratarErros(error);
          });
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