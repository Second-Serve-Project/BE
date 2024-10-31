import React from 'react';
import { View, TextInput, ScrollView, StyleSheet, Button } from 'react-native';
import Header from '../components/Header';
import Card from '../components/Card';
import BottomNavigation from '../components/BottomNavigation';

const HomeScreen = ({ navigation }) => {
  return (
    <View style={styles.container}>
      <Header navigation={navigation} />

      {/* Search Bar */}
      <View style={styles.searchBar}>
        <TextInput placeholder="Search" style={styles.searchInput} />
      </View>

      {/* Card List */}
      <ScrollView contentContainerStyle={styles.cardList}>
        <Card text="폭염경보: 서울시 부암동" />
        <Card text="31.6° 어제보다 1.9° 낮아요" />
        <Card text="인기판 미션 최대 2만원" />
      </ScrollView>

      {/* 로그인 버튼 */}
      <View style={styles.loginButton}>
        <Button
          title="로그인"
          color="#0CAF60"
          onPress={() => navigation.navigate('LoginForm')}
        />
        <Button
                title="Find Store"
                onPress={() => navigation.navigate('StoreSearch')}
        />
      </View>

      <BottomNavigation />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  searchBar: {
    marginTop: 20,
    alignItems: 'center',
  },
  searchInput: {
    width: '90%',
    borderWidth: 1,
    borderColor: '#0CAF60',
    borderRadius: 25,
    padding: 10,
    backgroundColor: '#fff',
  },
  cardList: {
    padding: 20,
  },
  loginButton: {
    margin: 20,
    alignItems: 'center',
  },
});

export default HomeScreen;
