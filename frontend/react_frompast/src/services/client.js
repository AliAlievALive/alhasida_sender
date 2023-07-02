import axios from "axios";

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("access_token")}`
    }
})

export const getTakersForSender = async (id) => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/takers/sender/${id}`, getAuthConfig())
    } catch (err){
        throw err
    }
};

export const saveTakerForSender = async (taker, id) => {
    try {
        taker.senderId = id;
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/takers/sender`, taker, getAuthConfig())
    } catch (err){
        throw err
    }
};

export const deleteTakerForSender = async (takerId) => {
    try {
        return await axios.delete(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/takers/${takerId}`,
            getAuthConfig()
        )
    } catch (err){
        throw err
    }
};

export const updateTakerForSender = async (takerId, taker) => {
    try {
        return await axios.put(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/takers/${takerId}`,
            taker,
            getAuthConfig()
        )
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