import React, { useEffect } from 'react'
import RNNordicBleModule, { Counter } from 'react-native-nordic-ble'

const App = () => {
  useEffect(() => {
    console.log(RNNordicBleModule)
  })

  return <Counter />
}

export default App
