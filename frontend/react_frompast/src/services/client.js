import axios from "axios";

export const getTakersForSender = async (id) => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/takers/${id}`)
    } catch (err){
        throw err
    }
};

export const saveTakerForSender = async (taker, id) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/takers/${id}`, taker)
    } catch (err){
        throw err
    }
};

export const deleteTakerForSender = async (senderId, takerId) => {
    try {
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/takers/${senderId}/${takerId}`)
    } catch (err){
        throw err
    }
};

export const updateTakerForSender = async (senderId, takerId, taker) => {
    try {
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/takers/${senderId}/${takerId}`, taker)
    } catch (err){
        throw err
    }
};

export const login = async (usernameAndPassword) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`, usernameAndPassword)
    } catch (err){
        throw err
    }
};