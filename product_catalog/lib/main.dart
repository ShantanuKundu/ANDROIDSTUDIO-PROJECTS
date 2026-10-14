import 'package:flutter/material.dart';

void main() {
  runApp(const ProductCatalogApp());
}

class ProductCatalogApp extends StatelessWidget {
  const ProductCatalogApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Adaptive Product Catalog',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: Colors.deepPurple,
          brightness: Brightness.light,
        ),
        useMaterial3: true,
      ),
      home: const CatalogDashboardScreen(),
    );
  }
}

// ---------------- MODEL ----------------
class Product {
  final String id;
  final String title;
  final String category;
  final double price;
  final double rating;
  final IconData icon;

  const Product({
    required this.id,
    required this.title,
    required this.category,
    required this.price,
    required this.rating,
    required this.icon,
  });
}

// ---------------- DUMMY DATA ----------------
const List<Product> mockProducts = [
  Product(
    id: '1',
    title: 'Noise Cancelling Headphones',
    category: 'Electronics',
    price: 4999.00,
    rating: 4.8,
    icon: Icons.headphones,
  ),
  Product(
    id: '2',
    title: 'Mechanical Gaming Keyboard',
    category: 'Electronics',
    price: 2499.00,
    rating: 4.6,
    icon: Icons.keyboard,
  ),
  Product(
    id: '3',
    title: 'Ergonomic Office Chair',
    category: 'Furniture',
    price: 8999.00,
    rating: 4.7,
    icon: Icons.chair,
  ),
  Product(
    id: '4',
    title: 'Smart Fitness Watch',
    category: 'Electronics',
    price: 3299.00,
    rating: 4.5,
    icon: Icons.watch,
  ),
  Product(
    id: '5',
    title: 'Ceramic Pour-Over Dripper',
    category: 'Home & Kitchen',
    price: 899.00,
    rating: 4.9,
    icon: Icons.coffee,
  ),
  Product(
    id: '6',
    title: 'Stainless Steel Water Bottle',
    category: 'Sports',
    price: 549.00,
    rating: 4.4,
    icon: Icons.water_drop,
  ),
  Product(
    id: '7',
    title: '4K Ultra HD Action Camera',
    category: 'Electronics',
    price: 12499.00,
    rating: 4.6,
    icon: Icons.camera_alt,
  ),
  Product(
    id: '8',
    title: 'Running Sneakers Pro',
    category: 'Sports',
    price: 2999.00,
    rating: 4.3,
    icon: Icons.directions_run,
  ),
];

// ---------------- MAIN SCREEN ----------------
class CatalogDashboardScreen extends StatefulWidget {
  const CatalogDashboardScreen({super.key});

  @override
  State<CatalogDashboardScreen> createState() => _CatalogDashboardScreenState();
}

class _CatalogDashboardScreenState extends State<CatalogDashboardScreen> {
  String _selectedCategory = 'All';
  bool? _forceGridView;

  // Cart state: maps Product to its selected quantity
  final Map<Product, int> _cart = {};

  final List<String> _categories = [
    'All',
    'Electronics',
    'Furniture',
    'Home & Kitchen',
    'Sports',
  ];

  int get _cartItemCount {
    int total = 0;
    _cart.forEach((_, qty) => total += qty);
    return total;
  }

  double get _cartTotalAmount {
    double total = 0.0;
    _cart.forEach((product, qty) => total += product.price * qty);
    return total;
  }

  List<Product> get _filteredProducts {
    if (_selectedCategory == 'All') return mockProducts;
    return mockProducts
        .where((item) => item.category == _selectedCategory)
        .toList();
  }

  void _addToCart(Product product) {
    setState(() {
      _cart[product] = (_cart[product] ?? 0) + 1;
    });
    ScaffoldMessenger.of(context).hideCurrentSnackBar();
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('${product.title} added to cart'),
        duration: const Duration(seconds: 1),
      ),
    );
  }

  void _removeFromCart(Product product) {
    setState(() {
      if (_cart.containsKey(product)) {
        if (_cart[product]! > 1) {
          _cart[product] = _cart[product]! - 1;
        } else {
          _cart.remove(product);
        }
      }
    });
  }

  void _showCartSheet() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (ctx) {
        return StatefulBuilder(
          builder: (context, setSheetState) {
            return Container(
              height: MediaQuery.of(context).size.height * 0.7,
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      const Text(
                        'Your Cart',
                        style: TextStyle(
                          fontSize: 20,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      IconButton(
                        icon: const Icon(Icons.close),
                        onPressed: () => Navigator.pop(context),
                      ),
                    ],
                  ),
                  const Divider(),
                  Expanded(
                    child: _cart.isEmpty
                        ? const Center(
                      child: Text(
                        'Your cart is empty',
                        style: TextStyle(color: Colors.grey, fontSize: 16),
                      ),
                    )
                        : ListView.separated(
                      itemCount: _cart.length,
                      separatorBuilder: (_, __) => const Divider(height: 1),
                      itemBuilder: (context, index) {
                        final product = _cart.keys.elementAt(index);
                        final quantity = _cart[product]!;
                        final subtotal = product.price * quantity;

                        return ListTile(
                          leading: CircleAvatar(
                            backgroundColor: Theme.of(context)
                                .colorScheme
                                .primaryContainer,
                            child: Icon(
                              product.icon,
                              color: Theme.of(context)
                                  .colorScheme
                                  .primary,
                            ),
                          ),
                          title: Text(
                            product.title,
                            style: const TextStyle(fontWeight: FontWeight.w600),
                          ),
                          subtitle: Text('₹${product.price.toStringAsFixed(0)} each'),
                          trailing: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              IconButton(
                                icon: const Icon(Icons.remove_circle_outline),
                                onPressed: () {
                                  _removeFromCart(product);
                                  setSheetState(() {});
                                },
                              ),
                              Text(
                                '$quantity',
                                style: const TextStyle(
                                  fontSize: 16,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                              IconButton(
                                icon: const Icon(Icons.add_circle_outline),
                                onPressed: () {
                                  _addToCart(product);
                                  setSheetState(() {});
                                },
                              ),
                              const SizedBox(width: 8),
                              Text(
                                '₹${subtotal.toStringAsFixed(0)}',
                                style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                  color: Colors.green,
                                ),
                              ),
                            ],
                          ),
                        );
                      },
                    ),
                  ),
                  if (_cart.isNotEmpty) ...[
                    const Divider(),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          'Total Amount',
                          style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                        ),
                        Text(
                          '₹${_cartTotalAmount.toStringAsFixed(0)}',
                          style: const TextStyle(
                            fontSize: 22,
                            fontWeight: FontWeight.bold,
                            color: Colors.green,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 12),
                    SizedBox(
                      width: double.infinity,
                      height: 48,
                      child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.deepPurple,
                          foregroundColor: Colors.white,
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(8),
                          ),
                        ),
                        onPressed: () {
                          Navigator.pop(context);
                          setState(() {
                            _cart.clear();
                          });
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(
                              content: Text('Order placed successfully!'),
                              backgroundColor: Colors.green,
                            ),
                          );
                        },
                        child: const Text('Proceed to Checkout', style: TextStyle(fontSize: 16)),
                      ),
                    ),
                  ],
                ],
              ),
            );
          },
        );
      },
    );
  }

  void _showProductDetails(Product product) {
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (context) {
        return Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  CircleAvatar(
                    radius: 30,
                    backgroundColor: Theme.of(context).colorScheme.primaryContainer,
                    child: Icon(product.icon, size: 32, color: Theme.of(context).colorScheme.primary),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          product.title,
                          style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                        ),
                        Text(
                          product.category,
                          style: TextStyle(color: Colors.grey.shade600),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 20),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    '₹${product.price.toStringAsFixed(0)}',
                    style: const TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                      color: Colors.green,
                    ),
                  ),
                  Row(
                    children: [
                      const Icon(Icons.star, color: Colors.amber, size: 20),
                      const SizedBox(width: 4),
                      Text(
                        '${product.rating}',
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                      ),
                    ],
                  ),
                ],
              ),
              const SizedBox(height: 24),
              SizedBox(
                width: double.infinity,
                height: 48,
                child: ElevatedButton.icon(
                  onPressed: () {
                    Navigator.pop(context);
                    _addToCart(product);
                  },
                  icon: const Icon(Icons.shopping_bag_outlined),
                  label: const Text('Add to Cart'),
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Product Catalog'),
        elevation: 1,
        actions: [
          // Toggle View Action
          IconButton(
            tooltip: 'Toggle Grid / List View',
            icon: Icon(
              _forceGridView == true ? Icons.view_list : Icons.grid_view,
            ),
            onPressed: () {
              setState(() {
                if (_forceGridView == null) {
                  _forceGridView = false;
                } else {
                  _forceGridView = !_forceGridView!;
                }
              });
            },
          ),
          // Cart Action with Badge
          Badge(
            isLabelVisible: _cartItemCount > 0,
            label: Text('$_cartItemCount'),
            alignment: const AlignmentDirectional(24, 4),
            child: IconButton(
              tooltip: 'View Cart',
              icon: const Icon(Icons.shopping_cart),
              onPressed: _showCartSheet,
            ),
          ),
          const SizedBox(width: 8),
        ],
      ),
      body: Column(
        children: [
          // Category Filter Bar
          SizedBox(
            height: 60,
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
              itemCount: _categories.length,
              itemBuilder: (context, index) {
                final category = _categories[index];
                final isSelected = category == _selectedCategory;
                return Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 4.0),
                  child: FilterChip(
                    label: Text(category),
                    selected: isSelected,
                    onSelected: (selected) {
                      setState(() {
                        _selectedCategory = category;
                      });
                    },
                  ),
                );
              },
            ),
          ),

          // Main Responsive Area
          Expanded(
            child: LayoutBuilder(
              builder: (context, constraints) {
                final bool isTabletOrDesktop = constraints.maxWidth >= 600;
                final bool useGridView = _forceGridView ?? isTabletOrDesktop;

                if (_filteredProducts.isEmpty) {
                  return const Center(child: Text('No products available.'));
                }

                if (useGridView) {
                  int crossAxisCount = 2;
                  if (constraints.maxWidth >= 1000) {
                    crossAxisCount = 4;
                  } else if (constraints.maxWidth >= 600) {
                    crossAxisCount = 3;
                  }

                  return GridView.builder(
                    padding: const EdgeInsets.all(12),
                    gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: crossAxisCount,
                      crossAxisSpacing: 12,
                      mainAxisSpacing: 12,
                      childAspectRatio: 0.8,
                    ),
                    itemCount: _filteredProducts.length,
                    itemBuilder: (context, index) {
                      final product = _filteredProducts[index];
                      return _buildGridCard(product);
                    },
                  );
                } else {
                  return ListView.builder(
                    padding: const EdgeInsets.all(12),
                    itemCount: _filteredProducts.length,
                    itemBuilder: (context, index) {
                      final product = _filteredProducts[index];
                      return _buildListTileCard(product);
                    },
                  );
                }
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildListTileCard(Product product) {
    return Card(
      elevation: 2,
      margin: const EdgeInsets.symmetric(vertical: 6),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: ListTile(
        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        leading: CircleAvatar(
          backgroundColor: Theme.of(context).colorScheme.primaryContainer,
          child: Icon(product.icon, color: Theme.of(context).colorScheme.primary),
        ),
        title: Text(
          product.title,
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        subtitle: Row(
          children: [
            Text(product.category),
            const SizedBox(width: 8),
            const Icon(Icons.star, size: 14, color: Colors.amber),
            Text(' ${product.rating}'),
          ],
        ),
        trailing: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              '₹${product.price.toStringAsFixed(0)}',
              style: const TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 15,
                color: Colors.green,
              ),
            ),
            const SizedBox(width: 8),
            IconButton(
              icon: const Icon(Icons.add_shopping_cart, size: 20),
              onPressed: () => _addToCart(product),
            ),
          ],
        ),
        onTap: () => _showProductDetails(product),
      ),
    );
  }

  Widget _buildGridCard(Product product) {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
      child: InkWell(
        borderRadius: BorderRadius.circular(14),
        onTap: () => _showProductDetails(product),
        child: Padding(
          padding: const EdgeInsets.all(12.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              CircleAvatar(
                radius: 36,
                backgroundColor: Theme.of(context).colorScheme.primaryContainer,
                child: Icon(product.icon, size: 36, color: Theme.of(context).colorScheme.primary),
              ),
              const SizedBox(height: 10),
              Text(
                product.title,
                textAlign: TextAlign.center,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
              ),
              const Spacer(),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    '₹${product.price.toStringAsFixed(0)}',
                    style: const TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 14,
                      color: Colors.green,
                    ),
                  ),
                  IconButton.filledTonal(
                    visualDensity: VisualDensity.compact,
                    icon: const Icon(Icons.add_shopping_cart, size: 18),
                    onPressed: () => _addToCart(product),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}