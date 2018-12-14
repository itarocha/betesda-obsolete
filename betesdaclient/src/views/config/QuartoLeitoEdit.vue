<template>
  <div>
    <v-dialog v-model="dialogVisible" width="500">
        <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          {{titulo}}
        </v-card-title>
        <v-card-text>
          <v-container grid-list-md>
            <v-layout wrap>
              <v-flex xs12 sm6 md4>
                <v-text-field v-model="form.numero" label="Número" type="number" :error-messages="getErrors('numero')"></v-text-field>
              </v-flex>
              <v-flex xs12 sm6 md8>
                <v-select v-model="form.tipoLeito" :items="itensTipoLeito" label="Tipo de Leito" :error-messages="getErrors('tipoLeito')"></v-select>
              </v-flex>
              <v-flex xs12 sm12 md12>
                <v-select v-model="form.situacao" :items="itensSituacaoLeito" label="Situação de Leito" :error-messages="getErrors('situacao')"></v-select>
              </v-flex>
            </v-layout>
          </v-container>
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

export default {
  name: 'QuartoLeitoEdit',
  
  props: {
    
  },

  data: () =>({
    form:{
      numero: 0,
      tipoLeito: null,
      situacao: null
    },
    dados: [],
    errors:[],
    dialogVisible : false,
    itensDestinacaoHospedagem: [],
    titulo:'Incluir/Alterar Leito',
    itensTipoLeito: [],
    itensSituacaoLeito: [],
    itensSimNao: [{ text: "Sim", value: "S" }, { text: "Não", value: "N" }],
  }),

  created(){
    this.reset()
  },

  methods: {
    openDialog(form, quarto){
      //console.log(form)
      this.titulo = (form.id == null ? 'Incluir novo Leito no Quarto ' : 'Alterar Leito do Quarto ') + quarto.numero;
      this.reset()
      this.loadListas()
      if (!form){
        this.reset()
      } else {
        this.form = form
      }
      this.dialogVisible = true
    },

    loadListas(evt) {
      this.itensDestinacaoHospedagem = [];
      let uri = petra.base_uri + "/quarto/listas";
      axios.get(uri).then(response => {
        this.itensDestinacaoHospedagem =
          response.data.listaDestinacaoHospedagem;
        this.itensTipoLeito = response.data.listaTipoLeito;
        this.itensSituacaoLeito = response.data.listaSituacaoLeito;
      });
    },

    close(value){
      this.dialogVisible = false
      this.$emit('close',false)
    },

    save(evt) {
      this.errors = [];

      let uri = petra.base_uri+"/quarto/leito";
      axios.post(uri, this.form)
          .then(response => { 
            this.dialogVisible = false
            this.$emit('close',true)
            this.$emit('save',response.data)
          }).catch(error => {
            this.errors = petra.tratarErros(error);
          });
    },

    reset(evt){
      this.form = {
        numero: 0,
        tipoLeito: null,
        situacao: null
      },
      this.errors = []
    },

    getErrors(fieldName){
      return petra.getErrors(fieldName, this.errors)
    }

  }
}
</script>