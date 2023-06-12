import axios from "axios";

export const getSenders = async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/senders`)
    } catch (err){
        throw err
    }
};