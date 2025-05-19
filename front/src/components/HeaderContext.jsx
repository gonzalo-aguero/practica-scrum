import {createContext,useContext, useState} from "react";

const HeaderContext = createContext();

export function HeaderProvider({ children }) {
    const [headerState, setHeaderState] = useState({});

    return(
        <HeaderContext.Provider value={{headerState, setHeaderState}}>
            {children}
        </HeaderContext.Provider>
    );
}

export function useHeader() {
    return useContext(HeaderContext);
}
