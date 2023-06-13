import axios from "axios";

export const getTakers = async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/takers`)
    } catch (err){
        throw err
    }
};

export const saveTaker = async (taker) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/takers`, taker)
    } catch (err){
        throw err
    }
};