//
//  PostCell.swift
//  genusbar_ios
//
//  Created by xingyu on 2018/11/18.
//  Copyright © 2018 xingyu. All rights reserved.
//

import Foundation
import XLPagerTabStrip

class PostCell: UITableViewCell {
    
    @IBOutlet weak var userImage: UIImageView!
    @IBOutlet weak var postName: UILabel!
    @IBOutlet weak var postText: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        userImage.layer.cornerRadius = 10.0
    }
    
    func configureWithData(_ data: NSDictionary) {
        if let post = data["post"] as? NSDictionary, let user = post["user"] as? NSDictionary {
            postName.text = user["name"] as? String
            postText.text = post["text"] as? String
            userImage.image = UIImage(named: postName.text!.replacingOccurrences(of: " ", with: "_"))
        }
    }
    
    func changeStylToBlack() {
        userImage?.layer.cornerRadius = 30.0
        postText.text = nil
        postName.font = UIFont(name: "HelveticaNeue-Light", size:18) ?? .systemFont(ofSize: 18)
        postName.textColor = .white
        backgroundColor = UIColor(red: 15/255.0, green: 16/255.0, blue: 16/255.0, alpha: 1.0)
    }
}
