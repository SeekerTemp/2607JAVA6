import axios from 'axios'

const API_BASE = 'http://localhost:8080/api/notes'

export function getAllNotes() {
  return axios.get(API_BASE).then(res => res.data)
}

export function createNote(author, content) {
  return axios.post(API_BASE, { author, content }).then(res => res.data)
}
