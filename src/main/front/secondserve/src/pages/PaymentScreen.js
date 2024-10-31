import React, { useState } from 'react';
import { View, Text, Button, StyleSheet, TouchableOpacity } from 'react-native';
import { useRoute } from '@react-navigation/native';

const PaymentScreen = () => {
  const route = useRoute();
  const { store } = route.params;
  const [quantity, setQuantity] = useState(1);

  const increaseQuantity = () => {
    setQuantity(quantity + 1);
  };

  const decreaseQuantity = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.storeName}>{store.name}</Text>
      <Text>Selected Time: Tomorrow 13:45 - 14:00</Text>

      <View style={styles.quantityControl}>
        <TouchableOpacity onPress={decreaseQuantity}>
          <Text style={styles.quantityButton}>-</Text>
        </TouchableOpacity>
        <Text style={styles.quantityText}>{quantity}</Text>
        <TouchableOpacity onPress={increaseQuantity}>
          <Text style={styles.quantityButton}>+</Text>
        </TouchableOpacity>
      </View>

      <Text>Total: €{(parseFloat(store.price.replace('€', '')) * quantity).toFixed(2)}</Text>

      <Button title="Reserve Now" onPress={() => alert('Payment Completed')} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    alignItems: 'center',
    justifyContent: 'center',
  },
  storeName: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  quantityControl: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 20,
  },
  quantityButton: {
    fontSize: 32,
    paddingHorizontal: 20,
  },
  quantityText: {
    fontSize: 24,
    marginHorizontal: 20,
  },
});

export default PaymentScreen;
