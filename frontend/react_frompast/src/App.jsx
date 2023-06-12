import SidebarWithHeader from "./shared/SideBar.jsx";
import {Button} from "@chakra-ui/react";
import {useEffect} from "react";
import {getSenders} from "./services/client.js";

const App = () => {

    useEffect(() => {
        getSenders().then(res => {
            console.log(res)
        }).catch(err => console.log(err));
    }, []);

    return (
        <SidebarWithHeader>
            <Button colorScheme='teal' variant='outline'>Click me</Button>
        </SidebarWithHeader>
    )
};

export default App
