import { apiRequest, resources } from './BaseService'

export default {
  getBulletins() {
    return apiRequest().then((axiosInstance) => axiosInstance.get(resources.bulletins))
    // return [
    //   { id: 1, startDate: '2022-01-01-', endDate: '2022-05-06', content: "This is the first bulletin. It takes you to <a href='http://google.ca'>Google</a>." },
    //   { id: 2, startDate: '2022-01-01-', endDate: '2022-05-06', content: 'This is the second bulletin.' },
    //   { id: 3, startDate: '2022-01-01-', endDate: '2022-05-06', content: 'This is the third bulletin.' },
    // ]
  },
}
