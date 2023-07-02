import {createContext, useContext, useEffect, useState} from "react";
import {login as performLogin} from "../../services/client.js";

const AuthContext = createContext({});
const AuthProvider = ({children}) => {
    const [sender, setSender] = useState(null);
    const login = async (usernameAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword).then(res => {
                const jwtToken = res.headers["authorization"];
                localStorage.setItem("access_token", jwtToken);
                console.log(jwtToken);
                setSender({
                    ...res.data.senderDTO
                })
                resolve(res);
            }).catch(err => {
                reject(err);
            });
        });
    };
    return <AuthContext.Provider value={{
        sender,
        login
    }}>
        {children}
    </AuthContext.Provider>
};

export const useAuth = () => useContext(AuthContext)
export default AuthProvider;