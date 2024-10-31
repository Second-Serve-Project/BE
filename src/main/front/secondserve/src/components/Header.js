import React from 'react';
import { View, Image, TouchableOpacity, StyleSheet } from 'react-native';

const Header = ({ navigation }) => {
  return (
    <View style={styles.header}>
      <TouchableOpacity onPress={() => navigation.navigate('Profile')}>
        <Image source={{ uri: 'https://img.icons8.com/ios/50/000000/menu--v1.png' }} style={styles.icon} />
      </TouchableOpacity>
      <Image source={{ uri: 'https://img.icons8.com/ios/50/000000/shopping-cart.png' }} style={styles.icon} />
      <Image source={{ uri: 'https://img.icons8.com/ios/50/000000/bell.png' }} style={styles.icon} />
    </View>
  );
};

const styles = StyleSheet.create({
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 15,
    backgroundColor: '#fff',
    elevation: 4,
  },
  icon: {
    width: 25,
    height: 25,
  },
});

export default Header;
