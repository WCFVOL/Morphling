import axios from 'axios'

function ajax (url, method, options) {
    if (options !== undefined) {
      var {params = {}, data = {}} = options
    } else {
      params = data = {}
    }
    return new Promise((resolve, reject) => {
      axios({
        url,
        method,
        params,
        data
      }).then(res => {
        // API正常返回(status=20x), 是否错误通过有无error判断
        if (res.data.error !== null) {
          resolve(res)
        }
      }).catch(e => {
        console.log(e)
      })
    })
  }
  