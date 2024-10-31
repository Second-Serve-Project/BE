import React from 'react';
import { View, Text, Button, StyleSheet } from 'react-native';
import { useNavigation, useRoute } from '@react-navigation/native';

const StoreDetailsScreen = () => {
  const route = useRoute();
  const navigation = useNavigation();
  const { store } = route.params;

  return (
    <View style={styles.container}>
      <Text style={styles.storeName}>{store.name}</Text>
      <Text>Menu Information</Text>
      <Text>{store.price}</Text>

      <Button
        title="Proceed to Payment"
        onPress={() => navigation.navigate('Payment', { store })}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    alignItems: 'center',
  },
  storeName: {
    fontSize: 24,
    fontWeight: 'bold',
  },
});

export default StoreDetailsScreen;
