import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state:{ // data
        token: localStorage.getItem('accessToken') || null,
        snackbar : false,
        numero : 0,

        flashMessage : {
            title : null,
            message : null,
            type : 'info'
        },
        tela: {
            descricao : '',
            acao : ''
        },
        count: 0,
        user: {
            name: '',
            email: ''
        },
        aguardando: []
    },

    getters:{ // computed properties
        loggedIn(state){
            return state.token != null
        },
        titulo(state){
            var descricao = ((state.tela.descricao == "") ? "" : " - " +state.tela.descricao);
            var acao = ((state.tela.acao == "") ? "" : " - " +state.tela.acao);
            return "Casa Betesda" + descricao + acao;
        },
        snackbar(state){
            return state.snackbar
        },
        numero(state){
            return state.numero
        },
        flashMessage(state){
            return state.flashMessage
        },
        user(state){
            // nem tudo...
            return state.user
        },
        aguardando : state => state.aguardando,
        qtdAguardando : state => {
            return state.aguardando.length
        },
    },

    // chamadas com $store.dispatch('metodo',valor)

    actions:{ // methods asyncronous

        retrieveToken(context, credentials){
            return new Promise((resolve, reject) => {
                axios.post(petra.base_uri+"/auth/login", {
                    usernameOrEmail: credentials.username,
                    password: credentials.password
                })
                .then(response => {
                    const token = response.data.accessToken
                    localStorage.setItem('accessToken', token)
                    context.commit('retrieveToken', token)
                    resolve(response)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        destroyToken(context){
            if (context.getters.loggedIn){
                localStorage.removeItem('accessToken')
                context.commit('destroyToken')
            }
        },
        increment(state){
            state.commit('increment')
        },
        decrement(state){
            state.commit('decrement')
        },
        setDescricao(state, descricao){
            state.commit('setDescricao', descricao)
        },
        setAcao(state, acao){
            state.commit('setAcao', acao)
        },
        setSnackbar(state, valor){
            state.commit('setSnackbar', valor)
        },
        setNumero(state, numero){
            status.commit('setNumero',numero)
        },
        showFlashMessage(state, message){
            var num = Math.floor(Math.random() * 10)
            state.commit('setNumero', num)
            state.commit('setFlashMessage', message)
        },

        //Entrada. chamar com store.dispatch('addAguardando',pessoa)
        addAguardando(context, pessoa) {
            context.commit('addAguardando', pessoa) // 
        },

        clearFlashMessage(state){
            state.commit('clearFlashMessage')
        }
    },




    mutations:{ //changing the state - call mutations
        increment(state){
            state.count++
        },
        decrement(state) {
            state.count--
        },
        setUser(store, obj){
            store.user = obj
        },
        setDescricao(state, descricao){
            state.tela.descricao = descricao
            state.tela.acao = ''
        },
        setAcao(state, acao){
            state.tela.acao = acao
        },
        setSnackbar(state, valor){
            state.snackbar = valor
        },
        setNumero(state, numero){
            state.numero = numero
        },
        setFlashMessage(state, flashMessage){
            state.flashMessage = flashMessage
        },
        retrieveToken(state, token){
            state.token = token
            axios.defaults.headers.common['Authorization'] = `Bearer ${state.token}`

            var decode = petra.parseJwt(token)
            state.user = decode

            //console.log(".............. store.retrieveToken = ",decode)

            //console.log("AXIOS.HEADER = ",axios.defaults.headers.common)
        },
        destroyToken(state){
            state.token = null
            state.user = null
            delete axios.defaults.headers.common['Authorization'];
        },

        //Onde as coisas acontecem
        addAguardando(state, pessoa){
            state.aguardando.push(pessoa)
        },

        clearFlashMessage(state){
            state.flashMessage = {
                title : null,
                message : null,
                type : 'info'
            }
            console.log('cleaning flashMessage')
        }
    },
})