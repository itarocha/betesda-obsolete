<template>
  <div>
    <v-dialog v-model="dialogVisible" width="800">
      <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          Incluir Encaminhador
        </v-card-title>

        <v-card-text>
          {{form.entidade.id}} - {{form.entidade.nome}}
          <v-layout row wrap>
            <v-flex xs12 sm8 md8>
              <v-text-field label="Nome" ref="edtNome" v-model="form.nome" :error-messages="getErrors('nome')"></v-text-field>
            </v-flex>

            <v-flex xs12 sm4 md4>
              <v-text-field label="Cargo" v-model="form.cargo" :error-messages="getErrors('cargo')"></v-text-field>
            </v-flex>

            <v-flex xs12 sm6 md6>
              <v-text-field label="Telefone" v-model="form.telefone" :error-messages="getErrors('telefone')"></v-text-field>
            </v-flex>
            <v-flex xs12 sm6 md6>
              <v-text-field label="Email" v-model="form.email" :error-messages="getErrors('email')"></v-text-field>
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
  name: "EncaminhadorEdit",

  directives: {
    mask
  },

  props: {},

  data: () => ({
    form: {
      entidade : {
        id : null,
        nome : null
      },
      id : null,
      nome : null,
      cargo : null,
      telefone : null,
      email : null
    },
    menuDataNascimento: false,
    dados: [],
    errors: [],
    dialogVisible: false,
  }),

  created() {
    this.reset();
  },

  watch : {
  },

  methods: {
    openDialog(form) {
      this.reset();
      if (form.entidade.id == null) {
        this.reset()
        this.form = form
      } else {
        this.form = form;
      }
      this.dialogVisible = true;
      setTimeout(() => {
        this.$refs.edtNome.focus()
      }, 500)
    },

    close(value) {
      this.dialogVisible = false;
      this.$emit("close", false);
    },

    save(evt) {
      this.errors = []

      petra.axiosPost("/app/encaminhadores/", this.form)
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