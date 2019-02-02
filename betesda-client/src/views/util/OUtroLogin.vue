<template>
  <div>
    <b-row>
      <b-col offset="3" cols="6">
        <b-card class="shadow"
                header-tag="header"
                header-bg-variant="primary"
                header-text-variant="white"
                footer-tag="footer">
            
          <div slot="header" class="font-weight-bold">L o g i n</div>
            
            <div class="card-text">
              <p>Header and footers using slots.</p>
              <p>Eita....</p>
            </div>

          <div style="text-align:right;" slot="footer">
            <b-button href="#" variant="primary" size="sm" class="text-uppercase font-weight-bold px-4" ><i class="fa fa-users"></i> Ok</b-button>
            <b-button href="#" variant="secondary" size="sm" class="text-uppercase font-weight-bold px-2">Cancelar</b-button>
            <b-button href="#" variant="danger" size="sm" class="rounded-circle px-2"><i class="fas fa-trash"></i></b-button>
          </div>
        </b-card>
      </b-col>
    </b-row>


  </div>
</template>

<script>
  import {mask} from 'vue-the-mask'

export default {
  directives: {mask},

  name:"Login",

  data () {
    return {
      form: {
        email: '',
        name: '',
        dataEntrada: null,
        rg: '',
        cpf: '',
        outrocpf : '',
        food: null,
        checked: []
      },
      foods: [
        { text: 'Select One', value: null },
        'Carrots', 'Beans', 'Tomatoes', 'Corn'
      ],
      show: true
    }
  },
  methods: {
    fmtMaiusculas(value, event){
      return petra.removerAcentos(value).toUpperCase()
    },

    fmtLetrasENumeros(value, event){
      return petra.letrasENumeros(value).toUpperCase()
    },

    onSubmit (evt) {
      evt.preventDefault();
      alert(JSON.stringify(this.form));
    },
    onReset (evt) {
      evt.preventDefault();
      /* Reset our form values */
      this.form.email = '';
      this.form.name = '';
      this.form.food = null;
      this.form.checked = [];
      /* Trick to reset/clear native browser form validation state */
      this.show = false;
      this.$nextTick(() => { this.show = true });
    }
  }
}
</script>
<style>

.imagem {
  text-align: center;
}

  .varde {
    background-color:  var(--cyan);
    color: blue;
  }

  .oua{
    background-color: #ef0; color:#000;
  }
</style>
