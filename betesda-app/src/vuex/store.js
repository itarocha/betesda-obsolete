import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state:{
        token: localStorage.getItem('accessToken') || null,
        snackbar : false,

        flashMessage : {
            text : 'Hello World',
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
        }
    },
    mutations:{ //syncronous
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
            //axios.defaults.headers.common['Authorization'] = null

            delete axios.defaults.headers.common['Authorization'];
        }
    },
    actions:{ // asyncronous

        retrieveToken(context, credentials){
            return new Promise((resolve, reject) => {
                axios.post(petra.base_uri+"/auth/login", {
                    usernameOrEmail: credentials.username,
                    password: credentials.password
                })
                .then(response => {
                    //console.log("store.js.retrieveToken",response)
                    const token = response.data.accessToken
                    localStorage.setItem('accessToken', token)
                    context.commit('retrieveToken', token)
                    resolve(response)
                }).catch(error => {
                    //console.log(error)
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
        showFlashMessage(state, flashMessage){
            state.commit('setFlashMessage', {
                text : flashMessage.text,
                type : flashMessage.type
            });
            state.commit('setSnackbar', true);
        },

    },
    getters:{
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
        flashMessage(state){
            return state.flashMessage
        },
        user(state){
            // nem tudo...
            return state.user
        }
    }
})