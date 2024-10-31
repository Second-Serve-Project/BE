import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import HomeScreen from './pages/home';
import Profile from './pages/profile';
import LoginForm from './pages/loginform';
import StoreSearchScreen from './pages/StoreSearchScreen';
import StoreDetailsScreen from './pages/StoreDetailsScreen';
import PaymentScreen from './pages/PaymentScreen';

// linking 설정, Route와 같은 기능
const linking = {
  prefixes: ['http://localhost:3000'], // 웹 애플리케이션이 구동되는 URL (예시)
  config: {
    screens: {
      Home: '/',
      Profile: '/profile',
      LoginForm: '/login',
      StoreSearch: 'store-search',
      StoreDetails: 'store-details/:id',  // 매장 ID를 파라미터로 넘길 수 있음
      Payment: 'payment',
    },
  },
};

const Stack = createNativeStackNavigator();

export default function App() {
  return (
    <NavigationContainer linking={linking}>
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="Profile" component={Profile} />
        <Stack.Screen name="LoginForm" component={LoginForm} />
        <Stack.Screen name="StoreSearch" component={StoreSearchScreen} />
        <Stack.Screen name="StoreDetails" component={StoreDetailsScreen} />
        <Stack.Screen name="Payment" component={PaymentScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
