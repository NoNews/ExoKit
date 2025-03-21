package com.personal.exokit.data

class VideoRepository {

    fun getVideos(): List<VideoInfo> {
        return listOf(
            VideoInfo(
                title = "BoJack's Poolside Meltdown",
                url = "https://v.redd.it/2p0bt1voflsd1/DASHPlaylist.mpd?f=hd%2ChlsTrimLow%2ChlsSpecOrder&v=1&a=1738667712%2COTA5NGIzYzUyYzhlY2YyZTE5NjBjOTc4ZTlmNTczZDM2NGIzZGMzMGJiMWMwNjg1MmE3OTM4YTJmMWFhNTg0OQ%3D%3D",
                thumbnail = "https://external-preview.redd.it/HkcOVqw6hnw1lJLRqQ8RUYRhKOMqAms_l5nN6oHvqAg.png?width=320&auto=webp&s=94a3b322669a2aa3d5bdaa77763a0ebb1480997d",
                width = 1920,
                height = 1080,
                mediaId = "ALPHA"
            ),
            VideoInfo(
                title = "Mr. Peanutbutter's Doggie Disco",
                url = "https://packaged-media.redd.it/02aerfrteuae1/muxed-medium.mp4?m=DASHPlaylist.mpd&c=wh_ben_en&var=sgpssan&v=1&e=1736082000&s=d7f0074d38a162789f0678a081fe962569ab2370",
                thumbnail = "https://external-preview.redd.it/OTRtNTNibXRldWFlMcNBL3vCHEtYC42j7gNSIgQ5Pp_U3yorYsTNWB9wOKXj.png?width=720&auto=webp&s=24ae82262862af4c6bb8cda19c5fe063498bf2e2",
                width = 720,
                height = 1280,
                mediaId = "BRAVO"
            ),
            VideoInfo(
                title = "Todd's Business Idea Gone Wrong",
                url = "https://external-packaged-media.redd.it/t3_1htxaim/3p/streamable/wddm05/video.mp4?v=1&e=1736082000&s=99ce04f7168bed0faddf57d7330c7334591d101e",
                thumbnail = "https://external-preview.redd.it/hi-_ELmYF-WyUdicfQ-ykOMagIFPeZvwtn7bczPTg1o.jpg?width=1080&auto=webp&s=7f2f6b01ed871a5f8ced92963fd8bf12da914c32",
                width = 1918,
                height = 1080,
                mediaId = "CHARLIE"
            ),
            VideoInfo(
                title = "Diane's Book Signing Fiasco",
                url = "https://v.redd.it/0rlixz7h0wae1/DASHPlaylist.mpd?f=sd%2CsubsAll%2ChlsSpecOrder&v=1&a=1738667845%2CZDA2Yzc0MjQwNWRiNWZkNWE2YTBjMTgzYzE0NWY5ODVkMTQ0YTYwYTgyZWFmNjUxMTQyNmRlNGEwYTllYzNmYg%3D%3D",
                thumbnail = "https://external-preview.redd.it/ZDZsOGZsNGgwd2FlMfk2Eig_NOZSNHPpxKQ7jSW8ubbGDbXUTNysFc9lx4IX.png?width=480&auto=webp&s=54b54a82f1c758c27b3f6b1cf51e2594d37f4e39",
                width = 480,
                height = 854,
                mediaId = "DELTA"
            ),
            VideoInfo(
                title = "Princess Carolyn's Cat Nap Interrupted",
                url = "https://v.redd.it/lv1yriqvseae1/DASHPlaylist.mpd?f=sd%2CsubsAll%2ChlsSpecOrder&v=1&a=1738668181%2CNjRmMTgwYTE4MzhlNGU0NDJlZDFlNTM4NTc4MzM1ODVkODhkYjIyNTMzNjk3MzZmYzVmMTRjOGU5NmJmZWI0Yw%3D%3D",
                thumbnail = "https://external-preview.redd.it/MWF4cWprbXZzZWFlMVPOI6oSOkB2BcUi2e68s_X-sw-jbxei0Yq-wl1etRp8.png?width=720&auto=webp&s=1777b974218073655a0a3e3f60426e0b46e0222d",
                width = 720,
                height = 1280,
                mediaId = "FOXTROT"
            ),
            VideoInfo(
                title = "BoJack's Failed Stand-Up Routine",
                url = "https://v.redd.it/g11bfnhl9iwd1/DASHPlaylist.mpd?f=hd%2ChlsTrimLow%2ChlsSpecOrder&v=1&a=1738668181%2CYzlhNTgwODlhNDAwYWQ0ZmM1YjJkZGM5NmQ5YzE0MWI1ZjMzNjJmNWFiM2NlM2Y2YmM0MzYwNzYxOGIxMDc1ZA%3D%3D",
                thumbnail = "https://external-preview.redd.it/vPUjDyoDm0QrUhIMgyyRzAJGyMFr8xh4_mB6t0QFTHM.png?width=1080&auto=webp&s=0b6be554465e87ddb52d034b8a9303b56e2a03d9",
                width = 1080,
                height = 1920,
                mediaId = "GOLF"
            ),
            VideoInfo(
                title = "Sarah Lynn's Grammy Rehearsal",
                url = "https://v.redd.it/fe729vsh0hed1/DASHPlaylist.mpd?f=hd%2ChlsTrimLow%2ChlsSpecOrder&v=1&a=1738668246%2COTdiNTA4YWFiNmZiZjg2ZmVmMjQ0NjhjYjg1NDg1Yzg3Y2M1NmU3NDZhNWI0ODY0Mzc2OWJmMWMxMWM1NmU0NQ%3D%3D",
                thumbnail = "https://external-preview.redd.it/8W2iJLNTwaYz69guVsZCrBn3ncfXenSDtttaijEmiuE.png?width=1080&auto=webp&s=2dfd6f9adcd8918d9044f8318080e44796725c6e",
                width = 1080,
                height = 1080,
                mediaId = "HOTEL"
            ),
            VideoInfo(
                title = "Diane's Quiet Library Escape",
                url = "https://packaged-media.redd.it/83glthlv6mae1/muxed-medium.mp4?m=DASHPlaylist.mpd&v=1&e=1736082000&s=a0ea5881b1359599303d8a7a887fa384cf2283ef",
                thumbnail = "https://external-preview.redd.it/YXV6dnZqbHY2bWFlMeFZNTeQlq6ntjPecVjDVM7S2RcfsOYnQj11qJu4pA81.png?width=480&auto=webp&s=76866abe0f38648b54d31d7716ba5209de54c840",
                width = 480,
                height = 634,
                mediaId = "INDIA"
            ),
            VideoInfo(
                title = "Todd's Musical Toaster Pitch",
                url = "https://packaged-media.redd.it/3hxqbqwnr2be1/muxed-medium.mp4?m=DASHPlaylist.mpd&v=1&e=1736082000&s=184e7dd2dc068b91a28160dd324bb87ef7cb21c1",
                thumbnail = "https://external-preview.redd.it/NjN4emNxbG5yMmJlMWmo0dygG8he0Mak6Trshf2iOlSthEmN-XYNmKhMAMw2.png?width=1080&auto=webp&s=6907d02c64416934c6d75b60c0898148fdecbdcf",
                width = 1080,
                height = 1920,
                mediaId = "JULIET"
            ),
            VideoInfo(
                title = "Mr. Peanutbutter's Election Campaign",
                url = "https://packaged-media.redd.it/anlihltsgzae1/muxed-medium.mp4?m=DASHPlaylist.mpd&c=wh_ben_en&var=sgpssan&v=1&e=1736082000&s=055ca436f7b299a7aaf6992ea56ea816e79c2eba",
                thumbnail = "https://external-preview.redd.it/ZmkwbG1sdHNnemFlMcRJOYGYEk0vX5iZ3OcP5W0YoXtKOOd14oheq32jRzLd.png?width=1080&auto=webp&s=472dc8edccb73fd37d965488136fc262ded1d44b",
                width = 1080,
                height = 1920,
                mediaId = "KILO"
            ),
            VideoInfo(
                title = "Vincent Adultman Goes Bowling",
                url = "https://external-packaged-media.redd.it/t3_1hu0jzb/3p/streamable/6uyt8p/video.mp4?v=1&e=1736082000&s=72b3548a99f6c97eb0d3ef90a4226294ce9dd1cc",
                thumbnail = "https://external-preview.redd.it/Zrtt0I_PIEa2_qAytORKTUzDxmH032_3ZI4uxVPVEUo.jpg?width=1080&auto=webp&s=add153303f2478b41cac1c088da775b0ae35c2e3",
                width = 1280,
                height = 720,
                mediaId = "ECHO"
            ),
        )
    }
}


data class VideoInfo(
    val title: String,
    val url: String,
    val thumbnail: String,
    val width: Int,
    val height: Int,
    val mediaId: String
)
