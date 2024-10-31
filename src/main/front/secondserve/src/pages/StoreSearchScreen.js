import React, { useState } from 'react';
import { View, TextInput, FlatList, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useNavigation } from '@react-navigation/native';

// 초기 상태에 사용할 하드코딩된 매장 데이터
const stores = [
  { id: '1', name: 'Boua Thaï', distance: '1.9 km', price: '€5,99' },
  { id: '2', name: 'O Brazil', distance: '2.3 km', price: '€4,50' },
  { id: '3', name: 'Urban Cafe', distance: '3.0 km', price: '€3,99' },
];

const StoreSearchScreen = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [filteredStores, setFilteredStores] = useState(stores); // 필터링된 매장 목록 상태
  const navigation = useNavigation();

  // 사용자가 입력한 검색어에 맞게 필터링된 매장 목록 업데이트
  const handleSearch = (query) => {
    setSearchQuery(query);

    if (query === '') {
      // 검색어가 비어있으면 모든 매장을 표시
      setFilteredStores(stores);
    } else {
      // 검색어에 맞게 매장을 필터링
      const filteredData = stores.filter((store) =>
        store.name.toLowerCase().includes(query.toLowerCase())
      );
      setFilteredStores(filteredData);
    }
  };

  // 각 매장을 버튼 형태로 렌더링
  const renderItem = ({ item }) => {
    console.log('Rendering item:', item); // 로그 추가

    return (
      <TouchableOpacity
        style={styles.storeButton}
        onPress={() => navigation.navigate('StoreDetails', { store: item })}
      >
        <Text style={styles.storeButtonText}>{item.name}</Text>
      </TouchableOpacity>
    );
  };

  return (
    <View style={styles.container}>
      <TextInput
        style={styles.searchInput}
        placeholder="Search stores"
        value={searchQuery}
        onChangeText={handleSearch} // 검색어 변경 시 호출
      />
      {filteredStores.length === 0 ? (
        <Text>No stores found</Text>
      ) : (
        <FlatList
          data={filteredStores}
          renderItem={renderItem}
          keyExtractor={(item) => item.id}
          style={styles.flatList}
          contentContainerStyle={styles.flatListContent}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff', // 배경색 설정
    padding: 20,
  },
  searchInput: {
    height: 40,
    borderColor: '#0CAF60',
    borderWidth: 1,
    marginBottom: 20,
    paddingHorizontal: 10,
    borderRadius: 10,
  },
  flatList: {
    flex: 1, // FlatList가 화면 전체를 차지하도록 설정
    width: '100%', // FlatList가 화면의 가로 길이를 차지하도록 설정
  },
  flatListContent: {
    paddingBottom: 20, // 여백 추가
  },
  storeButton: {
    backgroundColor: '#0CAF60', // 버튼 배경색 설정
    padding: 15,
    marginVertical: 10,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center', // 버튼 텍스트가 중앙에 오도록 설정
    width: '100%', // 버튼이 화면의 가로 길이를 차지하도록 설정
  },
  storeButtonText: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
});

export default StoreSearchScreen;
