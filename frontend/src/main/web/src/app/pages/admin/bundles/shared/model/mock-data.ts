import { Bundle } from "./bundle"

export const MOCK_DATA: Bundle[] = [
    {
        "id": 5,
        "start_date": new Date("2019-12-04 00:00"),
        "finish_date": new Date("2020-04-29 00:00"),
        "bundle_price": 2000,
        "bundle_description": "another test bundle",
        "bundle_trips": [
            {
                "trip_id": 4,
                "departure_spaceport_name": "porta",
                "arrival_spaceport_name": "sem",
                "departure_planet": "MOON",
                "arrival_planet": "NIBIRU",
                "ticket_classes": [
                    {
                        "class_id": 2,
                        "class_name": "non sodales",
                        "ticket_price": 2531,
                        "item_number": 1,
                        "services": [
                            {
                                "service_id": 4,
                                "service_name": "ligula nec sem duis",
                                "service_price": 356,
                                "item_number": 1
                            }
                        ]
                    },
                    {
                        "class_id": 1,
                        "class_name": "eget vulputate",
                        "ticket_price": 1984,
                        "item_number": 1,
                        "services": []
                    }
                ]
            }
        ]
    },
    {
        "id": 4,
        "start_date": new Date("2019-12-04 00:00"),
        "finish_date": new Date("2020-04-29 00:00"),
        "bundle_price": 2000,
        "bundle_description": "another test bundle",
        "bundle_trips": [
            {
                "trip_id": 4,
                "departure_spaceport_name": "porta",
                "arrival_spaceport_name": "sem",
                "departure_planet": "MOON",
                "arrival_planet": "NIBIRU",
                "ticket_classes": [
                    {
                        "class_id": 2,
                        "class_name": "non sodales",
                        "ticket_price": 2531,
                        "item_number": 1,
                        "services": [
                            {
                                "service_id": 4,
                                "service_name": "ligula nec sem duis",
                                "service_price": 356,
                                "item_number": 1
                            }
                        ]
                    },
                    {
                        "class_id": 1,
                        "class_name": "eget vulputate",
                        "ticket_price": 1984,
                        "item_number": 1,
                        "services": []
                    }
                ]
            }
        ]
    },
    {
        "id": 3,
        "start_date": new Date("2019-12-04 00:00"),
        "finish_date": new Date("2020-04-29 00:00"),
        "bundle_price": 2000,
        "bundle_description": "another test bundle",
        "bundle_trips": [
            {
                "trip_id": 4,
                "departure_spaceport_name": "porta",
                "arrival_spaceport_name": "sem",
                "departure_planet": "MOON",
                "arrival_planet": "NIBIRU",
                "ticket_classes": [
                    {
                        "class_id": 2,
                        "class_name": "non sodales",
                        "ticket_price": 2531,
                        "item_number": 1,
                        "services": []
                    },
                    {
                        "class_id": 1,
                        "class_name": "eget vulputate",
                        "ticket_price": 1984,
                        "item_number": 1,
                        "services": []
                    }
                ]
            }
        ]
    },
    {
        "id": 2,
        "start_date": new Date("2015-11-20 00:00"),
        "finish_date": new Date("2016-04-29 00:00"),
        "bundle_price": 9001,
        "bundle_description": "libero quis orci nullam molestie nibh in lectus pellentesque at nulla suspendisse potenti cras in purus eu magna vulputate luctus",
        "bundle_trips": [
            {
                "trip_id": 4,
                "departure_spaceport_name": "porta",
                "arrival_spaceport_name": "sem",
                "departure_planet": "MOON",
                "arrival_planet": "NIBIRU",
                "ticket_classes": [
                    {
                        "class_id": 1,
                        "class_name": "eget vulputate",
                        "ticket_price": 1984,
                        "item_number": 3,
                        "services": []
                    }
                ]
            }
        ]
    },
    {
        "id": 1,
        "start_date": new Date("2015-08-15 00:00"),
        "finish_date": new Date("2020-04-15 00:00"),
        "bundle_price": 1488,
        "bundle_description": "pharetra magna vestibulum aliquet ultrices erat tortor sollicitudin mi sit amet lobortis sapien sapien non mi integer",
        "bundle_trips": [
            {
                "trip_id": 4,
                "departure_spaceport_name": "porta",
                "arrival_spaceport_name": "sem",
                "departure_planet": "MOON",
                "arrival_planet": "NIBIRU",
                "ticket_classes": [
                    {
                        "class_id": 1,
                        "class_name": "eget vulputate",
                        "ticket_price": 1984,
                        "item_number": 1,
                        "services": [
                            {
                                "service_id": 4,
                                "service_name": "ligula nec sem duis",
                                "service_price": 888,
                                "item_number": 1
                            }
                        ]
                    },
                    {
                        "class_id": 2,
                        "class_name": "non sodales",
                        "ticket_price": 2531,
                        "item_number": 1,
                        "services": [
                            {
                                "service_id": 4,
                                "service_name": "ligula nec sem duis",
                                "service_price": 356,
                                "item_number": 1
                            }
                        ]
                    }
                ]
            }
        ]
    }
];