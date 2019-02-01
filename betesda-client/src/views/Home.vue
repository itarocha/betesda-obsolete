<template>
  <div class="container">
    <transition name="fade">
      <b-row v-if="show">
        <b-col>
            <img src="@/assets/cartaz.jpg" alt="Casa Betesda" class="mt-4 mb-4">
            <blockquote>
              A Casa Betesda é uma organização sem fins lucrativos que oferece hospedagem diária e gratuita a mulheres ou crianças em tratamento no Hospital das Clínicas de Uberlândia.
            </blockquote>
        </b-col>
      </b-row>
    </transition>

  </div>
</template>

<script>
  import {mask} from 'vue-the-mask'

export default {
  directives: {mask},

  name: 'Home',

  data () {
    return {
      show: false
    }
  },

  methods: {
  },

  mounted() {
    this.show = true;
  },
}
</script>

<style scoped>

.fade-enter-active, .fade-leave-active {
  transition: opacity 1.25s ease-out;
}

.fade-enter, .fade-leave-to {
  opacity: 0;
}

.container {
  text-align: center;
  background-color:  var(--white);
}

</style>
