import UserProfile from "./UserProfile.jsx";
import {useEffect, useState} from "react";

const users = [
    {name: "Ali", age: 22, gender: "Male"},
    {name: "Jamila", age: 44, gender: "Female"},
    {name: "Alex", age: 22, gender: "Male"},
    {name: "Alim", age: 30, gender: "Male"},
    {name: "Anna", age: 18, gender: "Female"},
]

const UserProfiles = ({users}) => (
    <div>
        {users.map((user, index) => (
            <UserProfile
                key={index}
                name={user.name}
                age={user.age}
                gender={user.gender}
                imageNumber={index}
            />
        ))}
    </div>
)

function App() {

    const [counter, setCounter] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    useEffect(() => {
        setIsLoading(true);
        setTimeout(() => {
            setIsLoading(false);
        }, 4000);

        return () =>
            console.log("cleanup functions")
    }, [])

    if (isLoading) {
        return "loading...";
    }

    return (
        <div>
            <button onClick={() => setCounter(prevCounter => prevCounter + 1)}>Increment counter</button>
            <h1>{counter}</h1>
            <UserProfiles users={users}/>
        </div>
    );
}

export default App
