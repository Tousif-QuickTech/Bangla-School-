<?php

namespace Database\Seeders;

use App\Models\User;
use App\Models\Author;
use App\Models\Book;
use App\Models\Course;
use App\Models\Blog;
use App\Models\Banner;
use App\Models\Notification;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        // Disable foreign key checks
        DB::statement('SET FOREIGN_KEY_CHECKS=0;');

        // Clear existing tables
        DB::table('blog_user_liked')->truncate();
        DB::table('blog_user_saved')->truncate();
        DB::table('book_user')->truncate();
        DB::table('course_user')->truncate();
        Notification::truncate();
        Banner::truncate();
        Blog::truncate();
        Course::truncate();
        Book::truncate();
        Author::truncate();
        User::truncate();

        DB::statement('SET FOREIGN_KEY_CHECKS=1;');

        // 1. Seed Authors
        $authors = [];
        
        $authors[] = Author::create([
            'name' => 'রবীন্দ্রনাথ ঠাকুর',
            'bio' => 'রবীন্দ্রনাথ ঠাকুর (১৮৬১-১৯৪১) ছিলেন একজন বাঙালি কবি, উপন্যাসিক, সংগীতস্রষ্টা ও দার্শনিক। তিনি ১৯১৩ সালে গীতাঞ্জলি কাব্যগ্রন্থের জন্য সাহিত্যে নোবেল পুরস্কার লাভ করেন, যা ছিল এশিয়ার প্রথম নোবেল বিজয়।',
            'photo_url' => 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300',
            'achievements' => 'সাহিত্যে নোবেল পুরস্কার (১৯১৩), নাইটহুড (১৯১৫ - পরে বর্জন করেন), কলকাতা ও অক্সফোর্ড বিশ্ববিদ্যালয় থেকে ডক্টরেট।',
            'works' => 'গীতাঞ্জলি, গোরা, ঘরে বাইরে, চোখের বালি, সোনার তরী, সঞ্চয়িতা, রক্তকরবী।',
            'books_count' => 3,
        ]);

        $authors[] = Author::create([
            'name' => 'কাজী নজরুল ইসলাম',
            'bio' => 'কাজী নজরুল ইসলাম (১৮৯৯-১৯৭৬) ছিলেন বিশ শতকের অন্যতম জনপ্রিয় বাঙালি কবি, সঙ্গীতজ্ঞ, ঔপন্যাসিক, এবং বাংলাদেশের জাতীয় কবি। তার কবিতায় বিদ্রোহ, মানবতা এবং প্রেম প্রধান থিম হিসেবে উঠে এসেছে।',
            'photo_url' => 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300',
            'achievements' => 'বাংলাদেশের জাতীয় কবি, একুশে পদক (১৯৭৬), স্বাধীনতা পুরস্কার (১৯৭৭)।',
            'works' => 'অগ্নিবীণা, বিষের বাঁশী, দোলন-চাঁপা, বাঁধন হারা, রিক্তের বেদন, শিউলি মালা।',
            'books_count' => 2,
        ]);

        $authors[] = Author::create([
            'name' => 'শরৎচন্দ্র চট্টোপাধ্যায়',
            'bio' => 'শরৎচন্দ্র চট্টোপাধ্যায় (১৮৭৬-১৯৩৮) ছিলেন একজন অত্যন্ত জনপ্রিয় বাঙালি কথাসাহিত্যিক। তার উপন্যাসে বাংলার সাধারণ গ্রামীণ সমাজ এবং নারীদের জীবনযাত্রা অত্যন্ত দরদের সঙ্গে ফুটে উঠেছে।',
            'photo_url' => 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300',
            'achievements' => 'জগত্তারিণী স্বর্ণপদক (১৯২৩), কলকাতা বিশ্ববিদ্যালয় থেকে ডক্টরেট।',
            'works' => 'দেবদাস, চরিত্রহীন, শ্রীকান্ত, গৃহদাহ, পথের দাবী, পরিণীতা, মেজদিদি।',
            'books_count' => 2,
        ]);

        // 2. Seed Books
        $books = [];

        $books[] = Book::create([
            'title' => 'গীতাঞ্জলি',
            'author_id' => $authors[0]->id,
            'author_name' => 'রবীন্দ্রনাথ ঠাকুর',
            'description' => 'গীতাঞ্জলি রবীন্দ্রনাথ ঠাকুরের একটি বিশ্ববিখ্যাত কাব্যগ্রন্থ। এই কাব্যগ্রন্থের ইংরেজি অনুবাদের জন্যই তিনি ১৯১৩ সালে সাহিত্যে নোবেল পুরস্কার লাভ করেন। এতে আধ্যাত্মিক এবং ভক্তিমূলক গানের অপূর্ব সংকলন রয়েছে।',
            'price' => 250.00,
            'rating' => 4.9,
            'reviews_count' => 128,
            'is_best_seller' => true,
            'is_ebook' => true,
            'cover_url' => 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400',
            'publish_year' => 1910,
            'pages' => 156,
            'isbn' => '978-984-555-102-1'
        ]);

        $books[] = Book::create([
            'title' => 'চোখের বালি',
            'author_id' => $authors[0]->id,
            'author_name' => 'রবীন্দ্রনাথ ঠাকুর',
            'description' => 'চোখের বালি রবীন্দ্রনাথ ঠাকুরের একটি সামাজিক মনস্তাত্ত্বিক উপন্যাস। এতে পরকীয়া প্রেম, বিধবা জীবনের জটিলতা এবং পারিবারিক সম্পর্কের টানাপোড়েন অত্যন্ত সূক্ষ্ম মনস্তাত্ত্বিক বিশ্লেষণে তুলে ধরা হয়েছে।',
            'price' => 220.00,
            'rating' => 4.6,
            'reviews_count' => 45,
            'is_best_seller' => false,
            'is_ebook' => true,
            'cover_url' => 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400',
            'publish_year' => 1903,
            'pages' => 280,
            'isbn' => '978-984-555-103-8'
        ]);

        $books[] = Book::create([
            'title' => 'সঞ্চয়িতা',
            'author_id' => $authors[0]->id,
            'author_name' => 'রবীন্দ্রনাথ ঠাকুর',
            'description' => 'সঞ্চয়িতা হলো রবীন্দ্রনাথ ঠাকুরের শ্রেষ্ঠ কবিতা ও গানের একটি অত্যন্ত জনপ্রিয় সংকলন গ্রন্থ। এতে কবির জীবনের বিভিন্ন পর্যায়ে রচিত সব কালজয়ী কবিতা অন্তর্ভুক্ত রয়েছে।',
            'price' => 350.00,
            'rating' => 4.8,
            'reviews_count' => 84,
            'is_best_seller' => true,
            'is_ebook' => true,
            'cover_url' => 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400',
            'publish_year' => 1931,
            'pages' => 512,
            'isbn' => '978-984-555-104-5'
        ]);

        $books[] = Book::create([
            'title' => 'অগ্নিবীণা',
            'author_id' => $authors[1]->id,
            'author_name' => 'কাজী নজরুল ইসলাম',
            'description' => 'অগ্নিবীণা কাজী নজরুল ইসলামের প্রথম কাব্যগ্রন্থ। এটি বাংলা সাহিত্যের একটি অত্যন্ত গুরুত্বপূর্ণ কাজ, যাতে কালজয়ী কবিতা "বিদ্রোহী" এবং "প্রলয়োল্লাস" প্রকাশিত হয়েছিল। এর প্রতিটি কবিতায় রয়েছে বৈপ্লবিক সুর।',
            'price' => 180.00,
            'rating' => 4.9,
            'reviews_count' => 95,
            'is_best_seller' => true,
            'is_ebook' => true,
            'cover_url' => 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400',
            'publish_year' => 1922,
            'pages' => 112,
            'isbn' => '978-984-555-105-2'
        ]);

        $books[] = Book::create([
            'title' => 'রিক্তের বেদন',
            'author_id' => $authors[1]->id,
            'author_name' => 'কাজী নজরুল ইসলাম',
            'description' => 'রিক্তের বেদন কাজী নজরুল ইসলামের একটি হৃদয়স্পর্শী গল্প সংকলন। এতে আটটি আবেগঘন গল্প রয়েছে যা তৎকালীন সমাজের প্রান্তিক মানুষের দুঃখ-কষ্ট ও রোমান্টিক বেদনাকে ফুটিয়ে তোলে।',
            'price' => 150.00,
            'rating' => 4.5,
            'reviews_count' => 30,
            'is_best_seller' => false,
            'is_ebook' => true,
            'cover_url' => 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400',
            'publish_year' => 1924,
            'pages' => 140,
            'isbn' => '978-984-555-106-9'
        ]);

        $books[] = Book::create([
            'title' => 'শ্রীকান্ত',
            'author_id' => $authors[2]->id,
            'author_name' => 'শরৎচন্দ্র চট্টোপাধ্যায়',
            'description' => 'শ্রীকান্ত শরৎচন্দ্র চট্টোপাধ্যায়ের লেখা একটি অমর ভ্রমণকাহিনীধর্মী চার খণ্ডের দীর্ঘ উপন্যাস। চরিত্রটির জীবন ও বিভিন্ন বিচিত্র মানুষের সাথে তার সাক্ষাৎ বাংলা সাহিত্যের এক অসামান্য অধ্যায়।',
            'price' => 290.00,
            'rating' => 4.7,
            'reviews_count' => 62,
            'is_best_seller' => true,
            'is_ebook' => true,
            'cover_url' => 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400',
            'publish_year' => 1917,
            'pages' => 450,
            'isbn' => '978-984-555-107-6'
        ]);

        $books[] = Book::create([
            'title' => 'পথের দাবী',
            'author_id' => $authors[2]->id,
            'author_name' => 'শরৎচন্দ্র চট্টোপাধ্যায়',
            'description' => 'পথের দাবী শরৎচন্দ্র চট্টোপাধ্যায়ের একটি জনপ্রিয় রাজনৈতিক উপন্যাস। ব্রিটিশ বিরোধী বিপ্লবী সব্যসাচীর কাহিনী এখানে ফুটে উঠেছে। ব্রিটিশ সরকার কর্তৃক তৎকালীন সময়ে উপন্যাসটি বাজেয়াপ্ত করা হয়েছিল।',
            'price' => 200.00,
            'rating' => 4.8,
            'reviews_count' => 78,
            'is_best_seller' => false,
            'is_ebook' => true,
            'cover_url' => 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400',
            'publish_year' => 1926,
            'pages' => 320,
            'isbn' => '978-984-555-108-3'
        ]);

        // 3. Seed Courses
        $courses = [];

        $courses[] = Course::create([
            'title' => 'বাংলা সাহিত্য রেনেসাঁ ও ইতিহাস',
            'instructor' => 'ড. আনিসুজ্জামান স্যার',
            'description' => 'এই কোর্সে আমরা ঊনবিংশ শতাব্দীর বাংলা নবজাগরণ, সাহিত্যিক পরিবর্তন এবং রাজা রামমোহন রায় থেকে শুরু করে রবীন্দ্রনাথ ঠাকুরের সময়কালের সাহিত্যিক বিপ্লব বিস্তারিত আলোচনা করব। সাহিত্যের প্রতিটি ধারা সুন্দরভাবে বিশ্লেষণ করা হবে।',
            'price' => 1200.00,
            'rating' => 4.9,
            'duration' => '২৪ ঘণ্টা (১২ লেকচার)',
            'lectures_count' => 12,
            'is_best_seller' => true,
            'enrolled_count' => 345,
            'cover_url' => 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=400',
            'category' => 'সাহিত্য ইতিহাস',
        ]);

        $courses[] = Course::create([
            'title' => 'উচ্চতর বাংলা ব্যাকরণ ও প্রমিত উচ্চারণ',
            'instructor' => 'অধ্যাপক শওকত আলী',
            'description' => 'বাংলা ভাষার সঠিক ব্যাকরণিক গঠন, বানান শুদ্ধিকরণ, সন্ধি, সমাস, এবং প্রমিত উচ্চারণ চর্চার জন্য এই কোর্সটি অত্যন্ত গুরুত্বপূর্ণ। শিক্ষক, শিক্ষার্থী এবং আবৃত্তিপ্রেমীদের জন্য উপযোগী।',
            'price' => 950.00,
            'rating' => 4.7,
            'duration' => '১৮ ঘণ্টা (১৫ লেকচার)',
            'lectures_count' => 15,
            'is_best_seller' => false,
            'enrolled_count' => 198,
            'cover_url' => 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=400',
            'category' => 'ভাষা ও ব্যাকরণ',
        ]);

        $courses[] = Course::create([
            'title' => 'রবীন্দ্র সাহিত্যের মূল চেতনা ও দর্শন',
            'instructor' => 'ড. সনৎ কুমার মিত্র',
            'description' => 'রবীন্দ্রনাথ ঠাকুরের গান, কবিতা, ও দর্শনের গভীরে প্রবেশ করার এক অনন্য সুযোগ। গীতাঞ্জলি থেকে শুরু করে তার শেষ জীবনের লেখনীর দার্শনিক দৃষ্টিভঙ্গি ও বিশ্বভারতী ভাবধারার ওপর আলোকপাত।',
            'price' => 1500.00,
            'rating' => 4.8,
            'duration' => '২০ ঘণ্টা (১০ লেকচার)',
            'lectures_count' => 10,
            'is_best_seller' => true,
            'enrolled_count' => 156,
            'cover_url' => 'https://images.unsplash.com/photo-1506880018603-83d5b814b5a6?w=400',
            'category' => 'সাহিত্য বিশ্লেষণ',
        ]);

        // 4. Seed Blogs
        $blogs = [];

        $blogs[] = Blog::create([
            'title' => 'রবীন্দ্র সাহিত্যের প্রাসঙ্গিকতা এবং আধুনিক যুব সমাজ',
            'author_name' => 'তানজিল রহমান',
            'publish_date' => '১৫ মে, ২০২৬',
            'reading_time' => '৫ মিনিট',
            'content' => 'আজকের আধুনিক যুগেও রবীন্দ্রনাথ ঠাকুরের রচনাগুলি কেন আমাদের তরুণ প্রজন্মের হৃদয়ে দোলা দেয়? তার কবিতা, গান আর দর্শন জীবনের জটিল সময়গুলোতে আমাদের সান্ত্বনা যোগায়। প্রেম থেকে শুরু করে দেশপ্রেম—জীবনের প্রতিটি অনুভূতির চমৎকার এক আশ্রয় হলো রবীন্দ্র সাহিত্য। এই ব্লগে আমরা দেখব কীভাবে আধুনিক জীবনেও রবীন্দ্রনাথের লেখা প্রাসঙ্গিক।',
            'cover_url' => 'https://images.unsplash.com/photo-1506880018603-83d5b814b5a6?w=400',
        ]);

        $blogs[] = Blog::create([
            'title' => 'একুশের চেতনা: বাংলা ভাষার সংগ্রাম ও বর্তমান প্রেক্ষাপট',
            'author_name' => 'ড. ফারহানা চৌধুরী',
            'publish_date' => '২১ ফেব্রুয়ারি, ২০২৬',
            'reading_time' => '৭ মিনিট',
            'content' => '১৯৫২ সালের ২১শে ফেব্রুয়ারি আমাদের ইতিহাসে এক স্বর্ণাক্ষরে লেখা দিন। মাতৃভাষার অধিকার প্রতিষ্ঠার জন্য জীবন দেওয়ার নজির কেবল বাঙালি জাতিরই রয়েছে। বর্তমান বিশ্বায়নের যুগে কীভাবে আমরা বাংলা ভাষাকে বিশ্বদরবারে প্রতিষ্ঠিত করতে পারি এবং তরুণদের মধ্যে প্রমিত বাংলা ভাষা ব্যবহারের গুরুত্ব বাড়াতে পারি, তা নিয়ে বিস্তারিত আলোচনা।',
            'cover_url' => 'https://images.unsplash.com/photo-1457369804613-52c61a468e7d?w=400',
        ]);

        // 5. Seed Banners
        Banner::create([
            'text' => 'আগামী ১৫ই জুলাই থেকে শুরু হচ্ছে সপ্তাহব্যাপী বাংলা অনলাইন সাহিত্য মেলা ও সেমিনার। যোগ দিন দেশের প্রথিতযশা কবি ও সাহিত্যিকদের আড্ডায়!',
            'image_url' => 'https://images.unsplash.com/photo-1507842217343-583bb7270b66?w=800',
            'is_active' => true,
        ]);

        Banner::create([
            'text' => 'ড. আনিসুজ্জামান স্যারের নতুন কোর্স "বাংলা নাট্য সাহিত্যের বিকাশ" এখন আমাদের কোর্সেস ট্যাবে উপলব্ধ। ৫০% ছাড়ে এখনই এনরোল করুন!',
            'image_url' => 'https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?w=800',
            'is_active' => true,
        ]);

        // 6. Seed Notifications
        Notification::create([
            'title' => 'নতুন কুইজ প্রতিযোগিতা!',
            'message' => 'কাজী নজরুল ইসলামের অগ্নিবীণা কাব্যের শতবর্ষ পূর্তি উপলক্ষে আমাদের অ্যাপে আজ রাতে শুরু হচ্ছে কুইজ প্রতিযোগিতা। বিজয়ী পাবেন বই উপহার!',
            'is_read' => false
        ]);

        Notification::create([
            'title' => 'কোর্স আপডেট!',
            'message' => 'বাংলা সাহিত্য রেনেসাঁ কোর্সের ৩য় লেকচার আপলোড করা হয়েছে। লেকচার নোট সহ আপনার ড্যাশবোর্ড থেকে এখনই দেখে নিন।',
            'is_read' => false
        ]);

        // 7. Seed Administrative and Test Users
        $admin = User::create([
            'username' => 'অনিন্দ্য সিনহা (Admin)',
            'email' => 'admin@banglaschool.com',
            'password' => Hash::make('password123'),
            'role' => 'admin',
            'bio' => 'বাংলা স্কুল অ্যাপ্লিকেশনের প্রধান প্রশাসক ও বাংলা ভাষা গবেষক।',
            'profile_photo_url' => 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150',
            'notification_pref' => true
        ]);

        $user = User::create([
            'username' => 'তাহমিদ আহমেদ',
            'email' => 'user@banglaschool.com',
            'password' => Hash::make('password123'),
            'role' => 'user',
            'bio' => 'বাংলা উপন্যাস পড়তে ভালোবাসি এবং ব্যাকরণ ও উচ্চারণ কোর্সে ভর্তি হয়েছি।',
            'profile_photo_url' => 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150',
            'notification_pref' => true
        ]);

        // Link regular user relations
        $user->enrolledCourses()->attach([$courses[0]->id, $courses[1]->id]);
        $user->purchasedBooks()->attach([$books[0]->id, $books[3]->id]);
        $user->savedBlogs()->attach([$blogs[0]->id]);
        $user->likedBlogs()->attach([$blogs[0]->id]);
    }
}
