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
    price: 199.99,
    rating: 4.8,
    icon: Icons.headphones,
  ),
  Product(
    id: '2',
    title: 'Mechanical Gaming Keyboard',
    category: 'Electronics',
    price: 89.99,
    rating: 4.6,
    icon: Icons.keyboard,
  ),
  Product(
    id: '3',
    title: 'Ergonomic Office Chair',
    category: 'Furniture',
    price: 249.50,
    rating: 4.7,
    icon: Icons.chair,
  ),
  Product(
    id: '4',
    title: 'Smart Fitness Watch',
    category: 'Electronics',
    price: 129.00,
    rating: 4.5,
    icon: Icons.watch,
  ),
  Product(
    id: '5',
    title: 'Ceramic Pour-Over Dripper',
    category: 'Home & Kitchen',
    price: 24.95,
    rating: 4.9,
    icon: Icons.coffee,
  ),
  Product(
    id: '6',
    title: 'Stainless Steel Water Bottle',
    category: 'Sports',
    price: 18.50,
    rating: 4.4,
    icon: Icons.water_drop,
  ),
  Product(
    id: '7',
    title: '4K Ultra HD Action Camera',
    category: 'Electronics',
    price: 299.99,
    rating: 4.6,
    icon: Icons.camera_alt,
  ),
  Product(
    id: '8',
    title: 'Running Sneakers Pro',
    category: 'Sports',
    price: 110.00,
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
  bool? _forceGridView; // null = auto-adapt by screen width, true/false = manual toggle

  final List<String> _categories = [
    'All',
    'Electronics',
    'Furniture',
    'Home & Kitchen',
    'Sports',
  ];

  List<Product> get _filteredProducts {
    if (_selectedCategory == 'All') return mockProducts;
    return mockProducts
        .where((item) => item.category == _selectedCategory)
        .toList();
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
                    '\$${product.price.toStringAsFixed(2)}',
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
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text('${product.title} added to cart!')),
                    );
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
          IconButton(
            tooltip: 'Toggle Grid / List View',
            icon: Icon(
              _forceGridView == true ? Icons.view_list : Icons.grid_view,
            ),
            onPressed: () {
              setState(() {
                if (_forceGridView == null) {
                  _forceGridView = false; // switch from auto to list
                } else {
                  _forceGridView = !_forceGridView!;
                }
              });
            },
          ),
        ],
      ),
      body: Column(
        children: [
          // 1. Horizontal ListView for Category Filter
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

          // 2. Adaptive Main Content (ListView or GridView based on layout)
          Expanded(
            child: LayoutBuilder(
              builder: (context, constraints) {
                // Screen size breakpoint: 600px
                final bool isTabletOrDesktop = constraints.maxWidth >= 600;

                // Determine layout mode
                final bool useGridView = _forceGridView ?? isTabletOrDesktop;

                if (_filteredProducts.isEmpty) {
                  return const Center(child: Text('No products available.'));
                }

                if (useGridView) {
                  // GridView Implementation
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
                      childAspectRatio: 0.82,
                    ),
                    itemCount: _filteredProducts.length,
                    itemBuilder: (context, index) {
                      final product = _filteredProducts[index];
                      return _buildGridCard(product);
                    },
                  );
                } else {
                  // ListView Implementation
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

  // Card layout for ListView
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
        trailing: Text(
          '\$${product.price.toStringAsFixed(2)}',
          style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.green),
        ),
        onTap: () => _showProductDetails(product),
      ),
    );
  }

  // Card layout for GridView
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
              const SizedBox(height: 12),
              Text(
                product.title,
                textAlign: TextAlign.center,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
              ),
              const Spacer(),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    '\$${product.price.toStringAsFixed(2)}',
                    style: const TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 15,
                      color: Colors.green,
                    ),
                  ),
                  Row(
                    children: [
                      const Icon(Icons.star, size: 14, color: Colors.amber),
                      Text(' ${product.rating}', style: const TextStyle(fontSize: 12)),
                    ],
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