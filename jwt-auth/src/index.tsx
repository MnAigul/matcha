import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import Store from './store/store';
import { BrowserRouter} from 'react-router-dom';
import { createContext } from 'vm';

interface State {
  store: Store,
}

export const store = new Store();

export const Context = React.createContext<State>({
  store,
})


const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(

     <BrowserRouter> 
      <Context.Provider value={{
          store
        }}>
        <App/>
      </Context.Provider>
    </BrowserRouter>
);

